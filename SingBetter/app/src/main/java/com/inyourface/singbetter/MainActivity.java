package com.inyourface.singbetter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.inyourface.singbetter.db.SessionDataSource;

import java.util.List;

public class MainActivity extends AppCompatActivity
{
	private Button gotoButton;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// TODO: Context switch to Recycler View Activity
		gotoButton = (Button) findViewById(R.id.goto_button);

		gotoButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(MainActivity.this, com.inyourface.singbetter.viewcontroller.SessionsViewActivity.class);
				try
				{
					startActivity(intent);
				}
				catch(Exception e)
				{
					String error = e.getMessage();
				}
			}
		});
	}
}
