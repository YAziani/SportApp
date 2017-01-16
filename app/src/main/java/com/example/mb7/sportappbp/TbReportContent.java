package com.example.mb7.sportappbp;

        import android.content.Intent;
        import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
        import android.widget.Button;

/**
 * Created by MB7 on 07.01.2017.
 */

public class TbReportContent extends TabFragment{

    Button testBtn;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.setTitle("Berichte");
        view = inflater.inflate(R.layout.tbreportcontent, container, false);

        testBasti(view);

        return view;
    }

    private void testBasti(View view){
        testBtn = (Button) view.findViewById(R.id.testbutton);
        testBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent open = new Intent(getActivity(), DiaryEntryActivity.class);
                startActivity(open);

            }
        });
    }

}
