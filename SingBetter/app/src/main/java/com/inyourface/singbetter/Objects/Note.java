package com.inyourface.singbetter.Objects;

/**
 * Created by Justin on 4/17/2017.
 */



public enum Note
{
	// The octave: C C#(D♭) D D#(E♭) E F F#(G♭) G G#(A♭) A A#(B♭) B
	A ("A", 440.00, 415.30, 466.16),
	A_SHARP ("A#", 466.16, 440.00, 493.88),
	B ("B", 493.88, 466.16, 261.63),
	//B_SHARP ("B#"),
	C ("C", 261.63, 493.88, 277.18),
	C_SHARP ("C#", 277.18, 261.63, 293.66),
	D ("D", 293.66, 277.18, 311.13),
	D_SHARP ("D#", 311.13, 293.66, 329.63),
	E ("E", 329.63, 311.13, 349.23),
	//E_SHARP ("E#"),
	F ("F", 349.23, 329.63, 369.99),
	F_SHARP ("F#", 369.99, 349.23, 392.00),
	G ("G", 392.00, 369.99, 415.30),
	G_SHARP ("G#", 415.30, 392.00, 440.00);





	private String note;
	private double frequency;
    private double min;
    private double max;

	private Note(String value, double frequency, double min, double max)
	{
		this.note = value;
		this.frequency = frequency;
        this.min = min;
        this.max = max;

	}

	public String getNoteString()
	{
		return this.note;
	}
	public double getNoteFrequency() { return this.frequency; }
    public double getMinFrequency() { return this.min; }
    public double getMaxFrequency() { return this.max; }
}
