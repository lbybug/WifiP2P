package listener;

/**
 * Created by LLB on 2018/8/12.
 */

public interface onReceiverProgress {

    void onStart();

    void onProgress(int progress);

    void onFailed();

    void onFinish();

}
