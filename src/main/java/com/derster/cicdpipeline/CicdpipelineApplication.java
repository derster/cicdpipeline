package com.derster.cicdpipeline;

import com.derster.cicdpipeline.entity.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class CicdpipelineApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(CicdpipelineApplication.class, args);


		List<Person> people = Arrays.asList(
				new Person("Alice", 28, 50000.0),
				new Person("Bob", 35, 75000.0),
				new Person("Charlie", 22, 60000.0),
				new Person("David", 40, 90000.0),
				new Person("Eva", 28, 55000.0),
				new Person("Frank", 35, 80000.0)
		);

		// Post data to APIs

		String data = postDataToApi();

		System.out.println("data = " + data);

		Map<String, Integer> nameAgeMap = 
				people.stream().collect(Collectors.toMap(Person::getName, Person::getAge));

		System.out.println("nameAgeMap = " + nameAgeMap);

		Map<String, Double> ageSalaryMap = people.stream().filter(person -> person.getAge() > 30)
				.collect(Collectors.toMap(Person::getName, Person::getSalary));

		System.out.println("ageSalaryMap = " + ageSalaryMap);

		List<Person> filteredAndSorted = people.stream().filter(person -> person.getAge() > 30 && person.getSalary() > 60000.0)
				.sorted(Comparator.comparingDouble(Person::getSalary).reversed())
				.toList();

		filteredAndSorted.forEach(person -> System.out.println(person.getName()+ " = " + person.getSalary()));

		Map<Integer, IntSummaryStatistics> ageStatsByGroup = people.stream().collect(Collectors.groupingBy(Person::getAge, Collectors.summarizingInt(Person::getAge)));

		System.out.println("ageStatsByGroup = " + ageStatsByGroup);

		Map<Integer, Double> averageSalaryByAge = people.stream().collect(Collectors.groupingBy(Person::getAge, Collectors.averagingDouble(Person::getSalary)));

		System.out.println("averageSalaryByAge = " + averageSalaryByAge);


		Map<Boolean, List<Person>> partitionedBySalary = people.stream().collect(Collectors.partitioningBy(person -> person.getSalary() > 60000.0));

		partitionedBySalary.get(false).forEach(person -> System.out.println(person.getName() + " = " + person.getSalary()));


		//List<User> users = fetchUsersFromAPI("https://jsonplaceholder.typicode.com/users");

		List<User> users = fetchDFromApi("https://jsonplaceholder.typicode.com/users");



		List<User> filterAndSorted = users.stream().filter(user -> user.getUsername().startsWith("d"))
				.sorted((u1, u2) -> u2.getAddress().getCity().compareTo(u1.getAddress().getCity()))
				.toList();
		filterAndSorted.forEach(user -> System.out.println(user.getName()));


	}


	public static List<User> fetchUsersFromAPI(String apiUrl) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			User[] users = objectMapper.readValue(new URL(apiUrl), User[].class);
			return List.of(users);
		} catch (IOException e) {
			e.printStackTrace();
			return List.of();
		}
	}

	public static List<User> fetchDFromApi(String apiUrl) throws IOException {
		// Create RestTemplate
		RestTemplate restTemplate = new RestTemplate();


        // Make GET request and retrieve the response
        User[] users = restTemplate.getForObject(apiUrl, User[].class);
        assert users != null;
        return List.of(users);
    }


	public static String postDataToApi() {
		// Create RestTemplate
		RestTemplate restTemplate = new RestTemplate();
		// Define the URL
		String apiUrl = "https://jsonplaceholder.typicode.com/users";

		// Define the request headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// Define the request body (replace with your JSON data)
		String requestBody = "{ \"name\": \"Modeste\", \"username\": \"derster\" }";

		// Create HttpEntity with headers and body
		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

		// Make POST request and retrieve the response
		String response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class).getBody();

		// Process the response as needed
		return response;
	}

}


class Address {
	private String street;
	private String suite;
	private String zipcode;
	private String city;
	private Geo geo;

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getSuite() {
		return suite;
	}

	public void setSuite(String suite) {
		this.suite = suite;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Geo getGeo() {
		return geo;
	}

	public void setGeo(Geo geo) {
		this.geo = geo;
	}
}

class Geo {
	private String lat;
	private String lng;

	// getter and setter

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}
}

class Company {
	private String name;
	private String catchPhrase;
	private String bs;

	// getter and setter


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCatchPhrase() {
		return catchPhrase;
	}

	public void setCatchPhrase(String catchPhrase) {
		this.catchPhrase = catchPhrase;
	}

	public String getBs() {
		return bs;
	}

	public void setBs(String bs) {
		this.bs = bs;
	}
}

class User {
	private int id;
	private String name;
	private String username;
	private Address address;
	private String email;
	private String phone;
	private String website;

	private Company company;

	public User() {
	}

	// getters and setters


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
}
