package it.polito.tdp.seriea.model;

public class Insieme {
	
	private Team t1;
	private Team t2;
	private String ftr;
	
	public Insieme(Team t1, Team t2, String ftr) {
		super();
		this.t1 = t1;
		this.t2 = t2;
		this.ftr = ftr;
	}
	
	public Team getT1() {
		return t1;
	}
	public void setT1(Team t1) {
		this.t1 = t1;
	}
	public Team getT2() {
		return t2;
	}
	public void setT2(Team t2) {
		this.t2 = t2;
	}
	public String getFtr() {
		return ftr;
	}
	public void setFtr(String ftr) {
		this.ftr = ftr;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((t1 == null) ? 0 : t1.hashCode());
		result = prime * result + ((t2 == null) ? 0 : t2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Insieme other = (Insieme) obj;
		if (t1 == null) {
			if (other.t1 != null)
				return false;
		} else if (!t1.equals(other.t1))
			return false;
		if (t2 == null) {
			if (other.t2 != null)
				return false;
		} else if (!t2.equals(other.t2))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("Risultato [t1=%s - t2=%s, ftr=%s]", t1, t2, ftr);
	}

}
