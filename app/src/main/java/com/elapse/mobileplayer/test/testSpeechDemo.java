package com.elapse.mobileplayer.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.elapse.mobileplayer.util.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by YF_lala on 2018/12/23.
 */
@Deprecated
public class testSpeechDemo extends Activity {

    private static final String TAG = "testSpeechDemo";
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showDialog();

    }

    private void showDialog() {
      //1、创建RecognizerDialog
        RecognizerDialog dialog = new RecognizerDialog(this,new MyInitListener());
        //2、设置参数
        dialog.setParameter(SpeechConstant.LANGUAGE,"zh_cn");
        dialog.setParameter(SpeechConstant.ACCENT,"mandarin");
        //3、设置回调接口
        dialog.setListener(new MyRecognizerDialogListener());
        dialog.show();
    }

    class  MyInitListener implements InitListener{

        @Override
        public void onInit(int i) {
            if (i != ErrorCode.SUCCESS){
                Log.d(TAG, "onInit: failed");
            }
        }
    }

    class MyRecognizerDialogListener implements RecognizerDialogListener{

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            String resultString = recognizerResult.getResultString();
            Log.d(TAG, "onResult: "+resultString);
            printResult(recognizerResult);
        }

        @Override
        public void onError(SpeechError speechError) {
            Log.d(TAG, "onError: "+speechError.getErrorDescription());
            Toast.makeText(testSpeechDemo.this,"出错了...",Toast.LENGTH_SHORT).show();
        }
    }

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());
        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

//        mResultText.setText(resultBuffer.toString());
//        mResultText.setSelection(mResultText.length());
    }
//    private EditText mResultText;
}
