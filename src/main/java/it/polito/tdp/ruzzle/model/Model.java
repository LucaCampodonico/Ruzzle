package it.polito.tdp.ruzzle.model;

import java.io.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.ruzzle.db.DizionarioDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Model {
	private final int SIZE = 4;
	private Board board;
	private List<String> dizionario;
	private List<String> parole;
	private StringProperty statusText;

	public Model() {
		this.statusText = new SimpleStringProperty();

		this.board = new Board(SIZE);
		DizionarioDAO dao = new DizionarioDAO();
		this.dizionario = dao.listParola();

		statusText.set(String.format("%d parole lette", this.dizionario.size()));

	}

	public void reset() {
		this.board.reset();
		this.statusText.set("Board Reset");
	}

	public Board getBoard() {
		return this.board;
	}

	public final StringProperty statusTextProperty() {
		return this.statusText;
	}

	public final String getStatusText() {
		return this.statusTextProperty().get();
	}

	public final void setStatusText(final String statusText) {
		this.statusTextProperty().set(statusText);
	}

	public List<Pos> trovaParola(String parola) {
		for (Pos p : board.getPositions()) {
			if (board.getCellValueProperty(p).get().charAt(0) == parola.charAt(0)) {
				List<Pos> percorso = new ArrayList<Pos>();
				percorso.add(p);
				if (cerca(parola, 1, percorso))
					return percorso;

			}
		}
		return null;
	}

	private boolean cerca(String parola, int i, List<Pos> percorso) {

//		System.out.println(i + "=" + parola.length());
		if (i == parola.length()) {
			return true;
		}

		Pos last = percorso.get(percorso.size()-1);
		List<Pos> adj = board.getAdjacencies(last);

		for (Pos p : adj) {

			if (parola.charAt(i) == board.getCellValueProperty(p).get().charAt(0))
				
			{
//				System.out.println("tento " + board.getCellValueProperty(p).get().charAt(0));
				if (percorso.contains(p)==false) {
//					System.out.println(" lui si " + board.getCellValueProperty(p).get().charAt(0));
//					System.out.println(p.toString());
					percorso.add(p);
					
//					System.out.println(i);
					boolean b = cerca(parola,i+1, percorso);
					if (b == true)
						return true;
					percorso.remove(percorso.size() - 1);
				}
			}
		}

		return false;
	}
//
//	public List<Pos> trovaParola2(String parola) {
//		for (Pos p : board.getPositions()) {
//			if (board.getCellValueProperty(p).get().charAt(0) == parola.charAt(0)) {
//				List<Pos> percorso = new ArrayList<Pos>();
//				percorso.add(p);
//				if (cerca2(parola, 1, percorso))
//					return percorso;
//
//			}
//		}
//		return null;
//	}
//	private boolean cerca2(String parola, int i, List<Pos> percorso) {
//
//System.out.println(i + "=" + parola.length());
//		if (i == parola.length()) {
//			return true;
//		}
//
//		Pos last = percorso.get(percorso.size()-1);
//		List<Pos> adj = board.getAdjacencies(last);
//
//		for (Pos p : adj) {
//			System.out.println("tento " + board.getCellValueProperty(p).get().charAt(0)+ p.toString() );
//			if (parola.charAt(i) == board.getCellValueProperty(p).get().charAt(0))
//				
//			{ 
//				if (percorso.contains(p)==false) {
//					System.out.println(" lui si " + board.getCellValueProperty(p).get().charAt(0));
//					percorso.add(p);
//					System.out.println(percorso.toString());
//					
//					System.out.println(i);
//					boolean b = cerca2(parola, i+1, percorso);
//					if (b == true)
//						return true;
//					System.out.println("torno indietro e cancello"+percorso.get(percorso.size()-1));
//					percorso.remove(percorso.size() - 1);
//				}
//			}
//		}
//
//		return false;
//	}

	public List<String> trovaTutte() {
		List<String> tutte = new ArrayList<>();
		for (String par : this.dizionario) {
			par = par.toUpperCase();
			if (par.length() > 1)
				if (this.trovaParola(par) != null) {
					tutte.add(par);
				}
		}
		return tutte;
	}

}
