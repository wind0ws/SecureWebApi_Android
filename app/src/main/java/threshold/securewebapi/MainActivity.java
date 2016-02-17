package threshold.securewebapi;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import threshold.securewebapi.api.ApiService;
import threshold.securewebapi.api.FilesApi;
import threshold.securewebapi.api.ProductsApi;
import threshold.securewebapi.api.ValuesApi;
import threshold.securewebapi.data.model.FileModel;
import threshold.securewebapi.data.model.Product;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.editTextServer)
    EditText mEditTextServer;
    @Bind(R.id.editTextUserId)
    EditText mEditTextUserId;
    @Bind(R.id.editTextPassword)
    EditText mEditTextPassword;
    @Bind(R.id.btnLogin)
    Button mBtnLogin;
    @Bind(R.id.linearLayAction)
    LinearLayout mLinearLayAction;
    @Bind(R.id.tvContent)
    TextView mTvContent;
    @Bind(R.id.btnGet)
    Button mBtnGet;
    @Bind(R.id.btnPost)
    Button mBtnPost;
    @Bind(R.id.btnUpload)
    Button mBtnUpload;
    @Bind(R.id.btnDownload)
    Button mBtnDownload;

    private ApiService mApiService;
    private ProductsApi mProductsApi;
    private ValuesApi mValuesApi;
    private FilesApi mFilesApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btnLogin,R.id.btnGet,R.id.btnPost,R.id.btnUpload,R.id.btnDownload})
    public void onClick(View v) {
        initApiService();
        switch (v.getId()) {
            case R.id.btnGet:
                getProduct();
                break;
            case R.id.btnPost:
                postProduct();
                break;
            case R.id.btnUpload:
                upload();
                break;
            case R.id.btnDownload:
                download();
                break;
        }
    }

    private void download() {
        mFilesApi.downloadFile("486a0837-2a85-4280-a09d-36717c730837.png")
                .subscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, File>() {
                    @Override
                    public File call(ResponseBody responseBody) {
                        File file = null;
                        if (responseBody != null) {
                            file = new File(Environment.getExternalStorageDirectory() + File.separator + "xx.png");
                            if (!file.exists()) {
                                try {
                                    file.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            InputStream inputStream = responseBody.byteStream();
                            //BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                            byte[] buffer = new byte[2048];
                            try {
                                OutputStream outputStream = new FileOutputStream(file);
                                int readLength;
                                while ((readLength = inputStream.read(buffer)) > 0) {
                                    outputStream.write(buffer, 0, readLength);
                                }
                                outputStream.flush();
                                outputStream.close();
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return file;
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mTvContent.setText("");
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<File>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(MainActivity.this, "onCompleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "onError请求失败了....", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(File file) {
                        String str;
                        if (file != null) {
                            str = "下载成功:" + file.getPath();
                        } else {
                            str = "下载失败";
                        }
                        Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                        mTvContent.setText(str);
                    }
                });

    }

    private void upload() {
        File file1 = new File(Environment.getExternalStorageDirectory() + File.separator + "testUpload.txt");
        if (!file1.exists()) {
            try {
                if (file1.createNewFile()) {
                    FileOutputStream outputStream = new FileOutputStream(file1);
                    outputStream.write("File1:This is A File Created By WebApi Client!".getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "testUpload2.txt");
        if (!file2.exists()) {
            try {
                if (file2.createNewFile()) {
                    FileOutputStream outputStream = new FileOutputStream(file2);
                    outputStream.write("File2:This is A File Created By WebApi Client!".getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        RequestBody requestBody1 = RequestBody.create(MultipartBody.FORM, file1);
        RequestBody requestBody2 = RequestBody.create(MultipartBody.FORM, file2);
        Map<String, RequestBody> fileMap = new HashMap<>();
        fileMap.put("file\"; filename=\" 1.txt", requestBody1);
        fileMap.put("file\"; filename=\" 2.txt", requestBody2);
        mFilesApi.uploadFile(fileMap)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<List<FileModel>, Observable<FileModel>>() {
                    @Override
                    public Observable<FileModel> call(List<FileModel> fileModels) {
                        return Observable.from(fileModels);
                    }
                })
                .map(new Func1<FileModel, String>() {
                    @Override
                    public String call(FileModel fileModel) {
                        return fileModel.toString();
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mTvContent.setText("");
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(MainActivity.this, "onCompleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e.toString());
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "onError请求失败了....", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(String s) {
                        mTvContent.setText(String.format("%s\n%s", mTvContent.getText(), s));
                    }
                });
    }

    private void postProduct() {
        Product product = new Product();
        product.setId(5);
        product.setName("香蕉");
        product.setPrice(25.5d);
        product.setCategory("水果");

        mProductsApi.addProduct(product).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Product>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Timber.d(e.getMessage());
                    }

                    @Override
                    public void onNext(Product product) {
                        mTvContent.setText(product.toString());
                    }
                });

    }

    private void initApiService() {
        String serverIp = String.format("http://%s/",mEditTextServer.getText().toString()) ;
        if (mApiService == null ||
                !serverIp.equals(mApiService.getBaseUrl())) {
            mApiService = new ApiService(serverIp);
            mProductsApi = mApiService.getProductsApi();
            mValuesApi = mApiService.getValuesApi();
            mFilesApi = mApiService.getFilesApi();
        }
    }


    public void getProduct() {
        mProductsApi.getProduct(3).subscribeOn(Schedulers.io())
                .map(new Func1<Product, String>() {
                    @Override
                    public String call(Product product) {
                        Timber.d("map操作    " + Thread.currentThread().getName());
                        return product.toString();
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Timber.d("doOnSubscribe    " + Thread.currentThread().getName());
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("Complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Timber.e(e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        mTvContent.setText(s);
                    }
                });
    }

}
