package com.inyourface.singbetter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;


public class NoteSelectActivity extends Activity {

    private String[] stringNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_select);

        stringNotes = Util.getAllNoteStrings();

        // ListView Setup
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringNotes);
        ListView notesListView = (ListView) findViewById(R.id.notes_list);
        notesListView.setAdapter(adapter);
        // Make ListView clickable
        notesListView.setOnItemClickListener(onListClick);
    }

    private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            Intent intent = new Intent();
            intent.putExtra("test", stringNotes[position]);
            setResult(RESULT_OK, intent);
            finish();
        }
    };
}
