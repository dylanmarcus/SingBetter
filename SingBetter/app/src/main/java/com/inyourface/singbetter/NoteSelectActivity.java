package com.inyourface.singbetter;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;


public class NoteSelectActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_select);

        String[] notes = {"c", "c sharp", "d", "d sharp", "e", "f", "f sharp", "g", "g sharp", "a", "a sharp", "b"};
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, notes);
        ListView notesListView = (ListView) findViewById(R.id.notes_list);
        notesListView.setAdapter(adapter);
    }
}
