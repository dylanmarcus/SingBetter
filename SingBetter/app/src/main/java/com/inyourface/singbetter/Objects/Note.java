package com.inyourface.singbetter.Objects;

/**
 * Created by Justin on 4/17/2017.
 */



public enum Note
{
	A ("A", 440.00),
	A_SHARP ("A#", 466.16),
	B ("B", 493.88),
	//B_SHARP ("B#"),
	C ("C", 261.63),
	C_SHARP ("C#", 277.18),
	D ("D", 293.66),
	D_SHARP ("D#", 311.13),
	E ("E", 329.63),
	//E_SHARP ("E#"),
	F ("F", 349.23),
	F_SHARP ("F#", 369.99),
	G ("G", 392.00),
	G_SHARP ("G#", 415.30);

	private String note;
	private double frequency;

	private Note(String value, double frequency)
	{
		this.note = value;
		this.frequency = frequency;

	}

	public String getNoteString()
	{
		return this.note;
	}
	public double getNoteFrequency() { return this.frequency; }
}
