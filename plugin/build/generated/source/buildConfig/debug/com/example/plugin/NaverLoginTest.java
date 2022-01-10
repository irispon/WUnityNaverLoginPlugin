package com.example.plugin;

//scripted by WonWoo
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.unity3d.player.UnityPlayer;

import org.json.JSONException;
import org.json.JSONObject;

public class NaverLoginTest{


    private static String OAUTH_CLIENT_ID = "";
    private static String OAUTH_CLIENT_SECRET = "";
    private static String OAUTH_CLIENT_NAME = "";
    //이 부분은 밖으로 빼기

    private static OAuthLogin mOAuthLoginInstance;
    private static Context mContext;
    public static NaverLoginTest login;

    public static void SetInfo(String OAUTH_CLIENT_ID,String OAUTH_CLIENT_SECRET,String OAUTH_CLIENT_NAME)
    {
        NaverLoginTest.OAUTH_CLIENT_ID = OAUTH_CLIENT_ID;
        NaverLoginTest.OAUTH_CLIENT_SECRET = OAUTH_CLIENT_SECRET;
        NaverLoginTest.OAUTH_CLIENT_NAME = OAUTH_CLIENT_NAME;

    }

    public static NaverLoginTest GetInstance()
    {
        if(login==null)
            login = new NaverLoginTest();
        return  login;
    }
    private void initData(Context context) {
        mContext=context;
        mOAuthLoginInstance = OAuthLogin.getInstance();
        mOAuthLoginInstance.showDevelopersLog(true);
        mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);
        /*
         * 2015년 8월 이전에 등록하고 앱 정보 갱신을 안한 경우 기존에 설정해준 callback intent url 을 넣어줘야 로그인하는데 문제가 안생긴다.
         * 2015년 8월 이후에 등록했거나 그 뒤에 앱 정보 갱신을 하면서 package name 을 넣어준 경우 callback intent url 을 생략해도 된다.
         */
        //mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME, OAUTH_callback_intent_url);
    }

    public void Click(Activity activity){
        mOAuthLoginInstance.startOauthLoginActivity(activity,mOAuthLoginHandler);
    }
    static private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        JSONObject obj = new JSONObject();
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
                String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
                long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
                String tokenType = mOAuthLoginInstance.getTokenType(mContext);
             //   Toast.makeText(mContext, "토큰: "+accessToken, Toast.LENGTH_SHORT).show();
               // Log.d("tt", "토큰:"+ accessToken);



                try {

                    obj.put("message", 1);
                    obj.put("token", accessToken);
                    UnityPlayer.UnitySendMessage("NaverLoginPlugin","NaverLoginCallBack",obj.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {

                String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);

                try {
                    obj.put("message", 0);
                    obj.put("token", "NULL");
                    UnityPlayer.UnitySendMessage("NaverLoginPlugin","NaverLoginCallBack",obj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

               // Toast.makeText(mContext, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }
    };


}
