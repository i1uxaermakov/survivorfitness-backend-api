package com.changeplusplus.survivorfitness.backendapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ResetPasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Date expiryDate;


    /**
     * A constructor for the ResetPasswordToken class that sets the user, the token string, and the date of expiry
     * @param user User the token is created for
     * @param token A unique string that will identify this ResetPasswordToken
     * @param hoursUntilExpiry Hours until expiry of this token.
     */
    public ResetPasswordToken(User user, String token, int hoursUntilExpiry) {
        this.id = null;
        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate(hoursUntilExpiry);
    }


    /**
     * Given the hours until expiry, this method will calculate the Date object of the expiry date.
     * @param expiryTimeInHours hours until expiry of the token
     * @return Date object that represents the moment when the ResetPasswordToken expires.
     */
    private Date calculateExpiryDate(int expiryTimeInHours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(cal.getTime().getTime()));
        cal.add(Calendar.HOUR, expiryTimeInHours);
        return new Date(cal.getTime().getTime());
    }
}
