package com.conny.netty;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.conny.netty.core.NettyManager;
import com.conny.netty.listener.OnNettyListener;
import com.conny.netty.proto.DataProto;
import com.conny.netty.proto.MessageProto;
import com.conny.netty.util.ProtoUtil;
import com.google.protobuf.InvalidProtocolBufferException;

public class MainActivity extends AppCompatActivity {

    private TextView mMessage;
    private EditText mInput;
    private Button mUpload;

    private long start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMessage = findViewById(R.id.msg);
        mInput = findViewById(R.id.input);
        mUpload = findViewById(R.id.upload);

        NettyManager.init("192.168.23.1", 9999);
        NettyManager.register(new OnNettyListener() {
            @Override
            public void onReceive(DataProto.Data data) {
                receive(data);
            }
        });
        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start = System.currentTimeMillis();
                send();
            }
        });
    }

    private void send() {

        String input = mInput.getText().toString();

        if (TextUtils.isEmpty(input)) {
            input = "netty android!";
        }

        DataProto.Data.Builder dataBuilder = DataProto.Data.newBuilder();
        dataBuilder.setCode(1);
        dataBuilder.setUid("ox111-110");

        MessageProto.Book.Builder bb = MessageProto.Book.newBuilder();
        bb.setId(11);
        bb.setName(input);
        bb.setPrice(222);


        MessageProto.Person.Builder builder = MessageProto.Person.newBuilder();
        builder.setId(1);
        builder.setName("haha anly");
        builder.setAge(22);

        MessageProto.Person person = builder.build();

        bb.setAuth(person);

        MessageProto.Book book = bb.build();

        dataBuilder.setData(ProtoUtil.bytes2String(book.toByteArray()));

        NettyManager.send(dataBuilder.build());
    }

    private void receive(DataProto.Data data) {
        if (data == null)
            return;

        StringBuffer sb = new StringBuffer(data.getUid());

        try {
            MessageProto.Book book = MessageProto.Book.parseFrom(data.getData().toByteArray());
            if (book != null) {
                sb.append("---");
                sb.append(book.getName());
            }

            long s = System.currentTimeMillis() - start;
            sb.append(" time = ");
            sb.append(s);

            mMessage.setText(sb);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
}
