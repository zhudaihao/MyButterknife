package cn.gxy.mybutterknife;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import cn.gxy.annotations.BindView;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv_text)
    TextView tv_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.init(this);

        tv_text.setText("butterKnife成功");
    }


//    @OnClick(R.id.tv_text)
//    public void onViewClick(View view) {
//        Toast.makeText(this, "测试点击", Toast.LENGTH_SHORT).show();
//
//    }

}
