package com.example.myapplication.ui.habitCategoriDetali;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.common.time.LocalDate;
import com.example.myapplication.data.entities.HabitEnty;
import com.example.myapplication.databinding.ActCategoriDetalhesDetailBinding;
import com.example.myapplication.di.Injection;
import com.example.myapplication.ui.addEdithabit.AddEditHabitoCategoriaActivity;
import com.example.myapplication.ui.entidadeHabitoAdd.HabitEntyViewItem;
import com.example.myapplication.ui.factory.DialogFactory;
import com.example.myapplication.ui.habitCategori.HabitCategoriViewItem;
import com.example.myapplication.ui.habitEntyDetali.HabitEntyDetailActivity;
import com.example.myapplication.ui.variaeisCategoriy.variaeisCategoriyCrieteRegistro.VarCategoriCriateViewItem;
import com.example.myapplication.utils.DialogUtils;
import com.example.myapplication.utils.ObjectUtils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;

import static com.example.myapplication.ui.addEdithabit.AddEditHabitoCategoriaActivity.REQUEST_EDIT_CODE;

import static org.naishadhparmar.zcustomcalendar.CustomCalendar.NEXT;
import static org.naishadhparmar.zcustomcalendar.CustomCalendar.PREVIOUS;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.OnNavigationButtonClickedListener;
import org.naishadhparmar.zcustomcalendar.Property;

public class ApontEquipamentoDetailActivity
        extends AppCompatActivity implements FlexibleAdapter.OnItemClickListener {

    public static final int REQUEST_DETAIL_CODE = 4;

    public static final int RESULT_EDIT_OK = RESULT_FIRST_USER + 6;

    public static final int RESULT_DELETE_OK = RESULT_EDIT_OK + 7;

    public static final String EXTRA_APONTAMENTO_ID = "EXTRA_APONTAMENTO_ID";

    private CustomCalendar calendarView;

    private FlexibleAdapter<HabitEntyViewItem> mAdapter;

    private ActCategoriDetalhesDetailBinding mBinding;

    private ApontEquipamentoDetailViewModel mViewModel;

    public static Intent getNewIntent(@NonNull Context context, long apontamentoId) {
        return new Intent(context, ApontEquipamentoDetailActivity.class)
                .putExtra(EXTRA_APONTAMENTO_ID, apontamentoId);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = findOrCreateViewModel();

        mBinding = DataBindingUtil.setContentView(this, R.layout.act_categori_detalhes_detail);
        mBinding.setVm(mViewModel);
        calendarView = (CustomCalendar) findViewById(R.id.simpleCalendarView);

        HashMap<Integer,Object> dateHashmap=new HashMap<>();

        // initialize calendar
        Calendar calendar =  Calendar.getInstance();
        calendarView.setDate(calendar,dateHashmap);

        // Put values
       // dateHashmap.put(calendar.get(Calendar.DAY_OF_MONTH),"current");
       // dateHashmap.put(1,"present");

        dateHashmap.put(2, "unavailable");
        dateHashmap.put(5, "holiday");
        dateHashmap.put(10, "default"); //You don't need to explicitly mention "default" description dates.
        dateHashmap.put(11, "unavailable");
        dateHashmap.put(19, "holiday");
        dateHashmap.put(20, "holiday");
        dateHashmap.put(24, "unavailable");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setupAdapters();
        subscribeApontamentoDeleted();
        subscribeCaregaVariaveis();
        subscribeCaregaMes();
        subscribeEditApontamento();
        subscribeErrorMessage();
        subscribeHabitAdd();
        setupCalendareObserve();


        HashMap<Object, Property> mapDescToProp = new HashMap<>();

        Property propDefault = new Property();
        propDefault.layoutResource = R.layout.default_view;
        propDefault.dateTextViewResource = R.id.default_datetextview;
        mapDescToProp.put("check", propDefault);

        Property propUnavailable = new Property();
        propUnavailable.layoutResource = R.layout.unavailable_view;
        //You can leave the text view field blank. Custom calendar won't try to set a date on such views
        propUnavailable.enable = false;
        mapDescToProp.put("unavailable", propUnavailable);

        Property propHoliday = new Property();
        propHoliday.layoutResource = R.layout.holiday_view;
        propHoliday.dateTextViewResource = R.id.holiday_datetextview;
        mapDescToProp.put("holiday", propHoliday);

        calendarView.setMapDescToProp(mapDescToProp);

        calendarView.setOnNavigationButtonClickedListener(PREVIOUS,
                new OnNavigationButtonClickedListener() {
                    @Override
                    public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, Calendar newMonth) {
                        Map<Integer, Object>[] arr = new Map[2];

                        arr[0] = new HashMap<>(); //This is the map linking a date to its description
                        mViewModel.loadEntyStart(new LocalDate(newMonth.getTime()));
                        return arr;
                    }
                });
        calendarView.setOnNavigationButtonClickedListener(NEXT,
                new OnNavigationButtonClickedListener() {
                    @Override
                    public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, Calendar newMonth) {
                        Map<Integer, Object>[] arr = new Map[2];

                        arr[0] = new HashMap<>(); //This is the map linking a date to its description
                        mViewModel.loadEntyStart(new LocalDate(newMonth.getTime()));

                        return arr;
                    }
                });
    }

    private void setupCalendareObserve(){

        calendarView.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                String sDate = selectedDate.get(Calendar.YEAR)
                        +"-" +(selectedDate.get(Calendar.MONTH)+1)
                        +"-" + selectedDate.get(Calendar.DAY_OF_MONTH) ;

                mViewModel.loadEnty(new LocalDate(selectedDate.getTime()));
            }

        });

    }

    private void carrega(){
        mViewModel.loadEntyStart(new LocalDate(mViewModel.date));
    }

    private void setupAdapters() {

        mAdapter = new FlexibleAdapter<>(new ArrayList<>(), this, true);

        FlexibleItemDecoration itemDecoration = new FlexibleItemDecoration(this)
                .withDefaultDivider(R.layout.item_habit_e);

        mBinding.habitList.setLayoutManager(new LinearLayoutManager(this));
        mBinding.habitList.setAdapter(mAdapter);
        mBinding.habitList.addItemDecoration(itemDecoration);
    }


    private void carregaVariavei(){
        mAdapter.addItems(0,mViewModel.getVariaveis());
    }

    private void carregaMes(){
        HashMap<Integer,Object> dateHashmap=new HashMap<>();

        // initialize calendar
        Calendar calendar =  Calendar.getInstance();

        List<HabitEnty> variaveis = mViewModel.getVariaveisMes();
        for (int i = 0 ; variaveis.size() > i; i++){
            dateHashmap.put(variaveis.get(i).getData().getDate(), "check");
        }
        calendarView.setDate(calendarView.getSelectedDate(),dateHashmap);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_equipamento_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_equipamento_detail_delete:
                mViewModel.editApontamento();
                return true;
            case R.id.menu_equipamento_detail_edit:
                deleteApontamento();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_EDIT_OK && resultCode == RESULT_OK) {
            setResult(RESULT_EDIT_OK);
            finish();
        }
    }

    public void editApontamento(long id) {
        Intent it = AddEditHabitoCategoriaActivity.getNewIntent(this, id+"");
        startActivityForResult(it, REQUEST_EDIT_CODE);
    }

    public void deleteApontamento() {

        DialogFactory.createDialog(this, getString(R.string.confirmation_delete_apontamento_message))
                .setNegativeButton(R.string.dialog_button_cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton(R.string.dialog_button_delete, (dialogInterface, i) -> {
                    mViewModel.delete();
                    dialogInterface.dismiss();
                })
                .show();
    }

    private void subscribeCaregaVariaveis() {
        mViewModel.getCarregaEnty().observe(this,  aVoid -> {
            mAdapter.clear();
            carregaVariavei();
        });
    }

    private void subscribeCaregaMes() {
        mViewModel.getCarregaMes().observe(this,  aVoid -> {
            mAdapter.clear();
            carregaMes();
        });
    }

    private void subscribeApontamentoDeleted() {
        mViewModel.getApontamentoDeleted().observe(this, aVoid -> {
            setResult(RESULT_DELETE_OK);
            finish();
        });
    }

    private void subscribeEditApontamento() {
        mViewModel.getEditApontamento().observe(this, this::editApontamento);
    }

    private void subscribeErrorMessage(){
        mViewModel.getErrorDialogMessage().observe(this, (header, message) ->
                DialogUtils.showDialog(this, header, message)
        );
    }
    private void subscribeHabitAdd() {
        mViewModel.getHabitAdd().observe(this, aVoid -> openAddHabit());
    }

    public void openAddHabit() {
        Intent it = com.example.myapplication.ui.entidadeHabitoAdd.AddEditHabitoCategoriaActivity.getNewIntent(this,mViewModel.getApontamento().getId()+"");
        startActivity(it);
    }
    private ApontEquipamentoDetailViewModel findOrCreateViewModel() {
        long apontamentoId = getIntent().getLongExtra(EXTRA_APONTAMENTO_ID, 0);

        ApontEquipamentoDetailViewModel.Factory factory = new ApontEquipamentoDetailViewModel.Factory(
                getApplication(),
                Injection.HabitCategoriRepository(getApplicationContext()),
                apontamentoId
        );

        return ViewModelProviders.of(this, factory).get(ApontEquipamentoDetailViewModel.class);
    }

    @Override
    public boolean onItemClick(int position) {
        int viewType = mAdapter.getItemViewType(position);

        if(viewType == R.layout.item_habit_e){
            HabitEntyViewItem viewItem = mAdapter.getItem(position);

            if (ObjectUtils.nonNull(viewItem)) {
                // mViewModel.selecionar = ;
                Intent it = HabitEntyDetailActivity.getNewIntent(this,viewItem.getModel().getId());
                startActivity(it);

                return true;
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        carrega();
        super.onResume();
    }

}
