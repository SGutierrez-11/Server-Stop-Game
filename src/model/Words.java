package model;

public class Words {
	private String name;
	private String animal;
	private String country;
	private String thing;
	
	private String type="Words";
	
	public Words(String name, String animal, String country, String thing) {
		this.name=name;
		this.animal=animal;
		this.country=country;
		this.thing = thing;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAnimal() {
		return animal;
	}

	public void setAnimal(String animal) {
		this.animal = animal;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getThing() {
		return thing;
	}

	public void setThing(String thing) {
		this.thing = thing;
	}
}
