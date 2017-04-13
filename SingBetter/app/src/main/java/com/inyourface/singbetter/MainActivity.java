package com.inyourface.singbetter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
	private Button gotoButton;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gotoButton = (Button) findViewById(R.id.goto_button);

		gotoButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(MainActivity.this, com.inyourface.singbetter.viewcontroller.SessionsViewActivity.class);
				startActivity(intent);
			}
		});
	}
}
