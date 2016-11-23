package com.cooksys.model;

// Generated Jun 1, 2015 9:51:27 AM by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * UserFlight generated by hbm2java
 */
@Entity
@Table(name = "user_flight", catalog = "flightservice")
public class UserFlight implements java.io.Serializable {

	private Integer userFlightId;
	private BookedFlight bookedFlight;
	private User user;

	public UserFlight() {
	}

	public UserFlight(BookedFlight bookedFlight, User user) {
		this.bookedFlight = bookedFlight;
		this.user = user;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "user_flight_id", unique = true, nullable = false)
	public Integer getUserFlightId() {
		return this.userFlightId;
	}

	public void setUserFlightId(Integer userFlightId) {
		this.userFlightId = userFlightId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "flight_id", nullable = false)
	public BookedFlight getBookedFlight() {
		return this.bookedFlight;
	}

	public void setBookedFlight(BookedFlight bookedFlight) {
		this.bookedFlight = bookedFlight;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}