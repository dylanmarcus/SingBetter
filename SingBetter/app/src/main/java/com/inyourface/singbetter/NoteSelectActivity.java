package com.inyourface.singbetter;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;


public class NoteSelectActivity extends Activity {

    //public final static String ID_EXTRA="com.inyourface.singbetter._ID";
    public static String SELECTED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_select);

        // Get the Intent that started this activity
        Intent intent = getIntent();

        // ListView Setup
        String[] notes = {"c", "c sharp", "d", "d sharp", "e", "f", "f sharp", "g", "g sharp", "a", "a sharp", "b"};
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, notes);
        ListView notesListView = (ListView) findViewById(R.id.notes_list);
        notesListView.setAdapter(adapter);
        // Make ListView clickable
        notesListView.setOnItemClickListener(onListClick);
    }

    private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String[] notes = {"c", "c sharp", "d", "d sharp", "e", "f", "f sharp", "g", "g sharp", "a", "a sharp", "b"};

            SELECTED = notes[position];

            Intent changeNote = new Intent(NoteSelectActivity.this, MainActivity.class);

            changeNote.putExtra(SELECTED, String.valueOf(SELECTED));
            startActivity(changeNote);

        }
    };


}
