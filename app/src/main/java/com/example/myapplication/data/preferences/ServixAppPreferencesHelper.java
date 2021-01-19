package com.example.myapplication.data.preferences;

import android.content.Context;
import com.google.common.net.HostAndPort;
import io.reactivex.annotations.NonNull;

public class ServixAppPreferencesHelper extends AppPreferencesHelper{

    private static ServixAppPreferencesHelper INSTANCE;

    public static final String APP_SERVIX_SETTINGS = "SERVIX_SETTINGS";

    public static final String KEY_APP_INSTANCE_ID = "KEY_APP_INSTANCE_ID";

    public static final String KEY_APP_INSTANCE_ID_ENABLED = "KEY_APP_INSTANCE_ID_ENABLED";

    public static final String KEY_APP_ENVIRONMENT = "KEY_APP_ENVIRONMENT";

    public static final String KEY_LAST_CONTRATO_SELECTED_ID = "KEY_LAST_CONTRATO_SELECTED_ID";

    public static final String KEY_SERVIX_MAIN_ADDRESS = "KEY_SERVIX_MAIN_ADDRESS";

    public static final String KEY_SERVIX_ALTERNATIVE_ADDRESS = "KEY_SERVIX_ALTERNATIVE_ADDRESS";

    public static final int SERVIX_DEFAULT_SERVER_PORT = 58602;

    private static final String DEFAULT_ADDRESS = "0.0.0.0";

    public static final String KEY_LAST_PROJETO_SELECTED_ID = "KEY_LAST_PROJETO_SELECTED_ID";

    public static final String KEY_SELECT_SERVICO_EXECUTADO_ORDER = "KEY_SELECT_SERVICO_EXECUTADO_ORDER";

    public static final String KEY_SERVIX_TIPO_AMAZEMANENO = "KEY_SERVIX_TIPO_AMAZEMANENO";

    public static final String KEY_SERVIX_BACKUP_PARENT_PARH = "KEY_SERVIX_BACKUP_PARENT_PARH";

    public static final String KEY_SERVIX_TIPE_SERVICE = "KEY_SERVIX_TIPE_SERVICE";

    //TODO: Remover em versões posteriores, quando o migration de preferences não for mais necessário
    public static final String LOCAL_SERVER_ADDRESS = "SERVIX_LOCAL_SERVER_ADDRESS";
    public static final String REMOTE_SERVER_ADDRESS = "SERVIX_REMOTE_SERVER_ADDRESS";
    public static final String LOCAL_SERVER_PORT = "SERVIX_LOCAL_SERVER_PORT";
    public static final String REMOTE_SERVER_PORT = "SERVIX_REMOTE_SERVER_PORT";

    private ServixAppPreferencesHelper(@NonNull Context context){
        super(context.getApplicationContext(), APP_SERVIX_SETTINGS);
    }

    public static synchronized ServixAppPreferencesHelper getInstance(@NonNull Context context){
        if(INSTANCE == null){
            INSTANCE = new ServixAppPreferencesHelper(context.getApplicationContext());
        }
        return INSTANCE;
    }

    @Override
    public String getSettingsName() {
        return APP_SERVIX_SETTINGS;
    }

    public void saveLastProjetoSelectedId(long projetoId){
        putLong(KEY_LAST_PROJETO_SELECTED_ID, projetoId);
    }

    public long getLastProjetoSelectedId(){
        return getLong(KEY_LAST_PROJETO_SELECTED_ID);
    }

    public void cleanLastProjetoSelectedId(){
        remove(KEY_LAST_PROJETO_SELECTED_ID);
    }

    public void saveLastContratoSelectedId(long contratoId){
        putLong(KEY_LAST_CONTRATO_SELECTED_ID, contratoId);
    }

    public long getLastContratoSelectedId(){
        return getLong(KEY_LAST_CONTRATO_SELECTED_ID);
    }

    public void cleanLastContratoSelectedId(){
        remove(KEY_LAST_CONTRATO_SELECTED_ID);
    }

    public void setAppInstanceId(String id){
        putString(KEY_APP_INSTANCE_ID, id);
    }

    public String getAppInstanceId(){
        return getString(KEY_APP_INSTANCE_ID);
    }

    public void setAppInstanceIdEnabled(boolean enabled){
        putBoolean(KEY_APP_INSTANCE_ID_ENABLED, enabled);
    }

    public boolean isAppInstanceIdEnabled(){
        return getBoolean(KEY_APP_INSTANCE_ID_ENABLED);
    }

    public void setServixMainNetworkAddress(HostAndPort baseUrl){
        putString(KEY_SERVIX_MAIN_ADDRESS, baseUrl.toString());
    }

    public HostAndPort getServixMainNetworkAddress(){
        verifyAndMigrateMainAddress();
        String baseUrl = getString(KEY_SERVIX_MAIN_ADDRESS, DEFAULT_ADDRESS);
        return HostAndPort.fromString(baseUrl).withDefaultPort(SERVIX_DEFAULT_SERVER_PORT);
    }

    private void verifyAndMigrateMainAddress(){
        if(getPreferences().contains(LOCAL_SERVER_ADDRESS)){
            String address = getString(LOCAL_SERVER_ADDRESS);
            String port = getString(LOCAL_SERVER_PORT);

            HostAndPort hostAndPort = HostAndPort.fromString(address + ":" + port).withDefaultPort(SERVIX_DEFAULT_SERVER_PORT);
            putString(KEY_SERVIX_MAIN_ADDRESS, hostAndPort.toString());

            remove(LOCAL_SERVER_ADDRESS);
            remove(LOCAL_SERVER_PORT);
        }
    }

    public void setServixAlternativeNetworkAddress(HostAndPort baseUrl){
        putString(KEY_SERVIX_ALTERNATIVE_ADDRESS, baseUrl.toString());
    }

    public HostAndPort getServixAlternativeNetworkAddress(){
        verifyAndMigrateAlternativeAddress();
        String baseUrl = getString(KEY_SERVIX_ALTERNATIVE_ADDRESS, DEFAULT_ADDRESS);
        return HostAndPort.fromString(baseUrl).withDefaultPort(SERVIX_DEFAULT_SERVER_PORT);
    }

    private void verifyAndMigrateAlternativeAddress(){
        if(getPreferences().contains(REMOTE_SERVER_ADDRESS)){
            String address = getString(REMOTE_SERVER_ADDRESS);
            String port = getString(REMOTE_SERVER_PORT);

            HostAndPort hostAndPort = HostAndPort.fromString(address + ":" + port).withDefaultPort(SERVIX_DEFAULT_SERVER_PORT);
            putString(KEY_SERVIX_ALTERNATIVE_ADDRESS, hostAndPort.toString());

            remove(REMOTE_SERVER_ADDRESS);
            remove(REMOTE_SERVER_PORT);
        }
    }

    public void setTipoArmazenamento(int tipo){
        putInteger(KEY_SERVIX_TIPO_AMAZEMANENO,tipo);
    }

    public int getTipoArmazenamento(){
        int tipo = getInteger(KEY_SERVIX_TIPO_AMAZEMANENO);
        return tipo == -1 ? 0 :tipo;
    }
    public void setBackupParentParh(String caminho){
        putString(KEY_SERVIX_BACKUP_PARENT_PARH,caminho);
    }

    public String getBackupParentParh(){
        String caminho = getString(KEY_SERVIX_BACKUP_PARENT_PARH);
        return caminho.equals("") ? "/servix/backup/" :caminho;
    }
    public void setTipoServico(int caminho){
        putInteger(KEY_SERVIX_TIPE_SERVICE,caminho);
    }

    public int getTipoServico(){
        int tipo = getInteger(KEY_SERVIX_TIPE_SERVICE);
        return tipo == -1 ? 0 :tipo;
    }

}
