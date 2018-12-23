package com.elapse.mobileplayer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.elapse.mobileplayer.util.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

/**
 * Created by YF_lala on 2018/12/23.
 */

public class testSpeechDemo extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showDialog();

    }

    private void showDialog() {
        //1.创建SpeechRecognizer对象，第二个参数：本地听写时传InitListener
        SpeechRecognizer mIat = SpeechRecognizer.createRecognizer(this, null);
        //2.设置听写参数，详见《科大讯飞MSC API手册(Android)》SpeechConstant类
        mIat.setParameter(SpeechConstant.DOMAIN,"iat");
        mIat.setParameter(SpeechConstant.LANGUAGE,"zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT,"mandarin ");
        //3.开始听写
        mIat.startListening(mRecoListener);
        //听写监听器
        RecognizerDialog iatDialog = new RecognizerDialog(this, mInitListener);
        iatDialog.setListener(recognizerDialogListener);
        iatDialog.show();
    }

    private lisenner1 mInitListener = new lisenner1();
    class  lisenner1 implements InitListener{

        @Override
        public void onInit(int i) {
            if (i != ErrorCode.SUCCESS){
                Log.d(TAG, "onInit: failed");
            }
        }
    }

    private static final String TAG = "testSpeechDemo";
    private RDListener recognizerDialogListener = new RDListener();
    class RDListener implements RecognizerDialogListener{

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            Log.d(TAG, "onResult: "+recognizerResult.getResultString());
        }

        @Override
        public void onError(SpeechError speechError) {
            Log.d(TAG, "onError: "+speechError.getErrorDescription());
        }
    }

    RecognizerListener mRecoListener = new RecognizerListener() {
        //听写结果回调接口(返回Json格式结果，用户可参见附录12.1)；
        //一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
        //关于解析Json的代码可参见MscDemo中JsonParser类；
        //isLast等于true时会话结束。
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d("Result:", results.getResultString());
            JsonParser.parseIatResult(results.getResultString());
        }

        //会话发生错误回调接口
        public void onError(SpeechError error) {
            error.getPlainDescription(true);
        }

        @Override
        public void onVolumeChanged(int i, byte[] bytes) {

        }

        //开始录音
        public void onBeginOfSpeech () {

        }

        //结束录音
        public void onEndOfSpeech() {

        }

        //扩展用接口
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

        }
    };
}
