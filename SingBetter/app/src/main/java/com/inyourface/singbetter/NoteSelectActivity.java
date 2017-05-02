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
<<<<<<< HEAD
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringNotes);
=======
        String[] notes = {"C", "C#/D♭", "D", "D#/E♭", "E", "F", "F#/G♭", "G", "G#/A♭", "A", "A#/B♭", "B"};
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, notes);
>>>>>>> UI_Improvement
        ListView notesListView = (ListView) findViewById(R.id.notes_list);
        notesListView.setAdapter(adapter);
        // Make ListView clickable
        notesListView.setOnItemClickListener(onListClick);
    }

    private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
        @Override
<<<<<<< HEAD
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            Intent intent = new Intent();
            intent.putExtra("test", stringNotes[position]);
            setResult(RESULT_OK, intent);
            finish();
=======
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String[] notes = {"C", "C#/D♭", "D", "D#/E♭", "E", "F", "F#/G♭", "G", "G#/A♭", "A", "A#/B♭", "B"};

            SELECTED = notes[position];

            Intent changeNote = new Intent(NoteSelectActivity.this, MainActivity.class);

            changeNote.putExtra(SELECTED, String.valueOf(SELECTED));
            startActivity(changeNote);

>>>>>>> UI_Improvement
        }
    };
}
