/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mike.testapp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 *
 * @author Mike-MSI
 */
@Entity
@Table(name = "memberships")
@NamedQueries({
	@NamedQuery(
	name = "findAllMemberships",
	query = "from Membership where isDeleted = 0"
	)
})
public class Membership implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Integer id;
    
    private String name;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private Double price;
    
    private String chargeTax;
    
    private String cycle;
    
    private Double cycleAmount;
    
    private Boolean visitsRestriction;
    
    private Integer visitsAllowed;
    
    private Boolean daysRestriction;
    
    private String daysAllowed;
    
    private Boolean timesRestriction;
    
    private String timesAllowed;
    
    private Boolean expire;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date expireDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    
    private Integer userCreated;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Version
    private Date dateUpdated;
    
    private Integer userUpdated;
    
    private Boolean isActive;
    
    private Boolean isDeleted;
    
    @OneToMany(mappedBy = "membership", cascade = CascadeType.ALL)
    private Set<MemberMembership> memberMemberships;

    public Membership() {
        dateCreated = new Date();
        isActive = true;
        isDeleted = false;
    }

    public Membership(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getChargeTax() {
        return chargeTax;
    }

    public void setChargeTax(String chargeTax) {
        this.chargeTax = chargeTax;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public Double getCycleAmount() {
        return cycleAmount;
    }

    public void setCycleAmount(Double cycleAmount) {
        this.cycleAmount = cycleAmount;
    }

    public Boolean getVisitsRestriction() {
        return visitsRestriction;
    }

    public void setVisitsRestriction(Boolean visitsRestriction) {
        this.visitsRestriction = visitsRestriction;
    }

    public Integer getVisitsAllowed() {
        return visitsAllowed;
    }

    public void setVisitsAllowed(Integer visitsAllowed) {
        this.visitsAllowed = visitsAllowed;
    }

    public Boolean getDaysRestriction() {
        return daysRestriction;
    }

    public void setDaysRestriction(Boolean daysRestriction) {
        this.daysRestriction = daysRestriction;
    }

    public String getDaysAllowed() {
        return daysAllowed;
    }

    public void setDaysAllowed(String daysAllowed) {
        this.daysAllowed = daysAllowed;
    }

    public Boolean getTimesRestriction() {
        return timesRestriction;
    }

    public void setTimesRestriction(Boolean timesRestriction) {
        this.timesRestriction = timesRestriction;
    }

    public String getTimesAllowed() {
        return timesAllowed;
    }

    public void setTimesAllowed(String timesAllowed) {
        this.timesAllowed = timesAllowed;
    }

    public Boolean getExpire() {
        return expire;
    }

    public void setExpire(Boolean expire) {
        this.expire = expire;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Integer getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(Integer userCreated) {
        this.userCreated = userCreated;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Integer getUserUpdated() {
        return userUpdated;
    }

    public void setUserUpdated(Integer userUpdated) {
        this.userUpdated = userUpdated;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public Set<MemberMembership> getMemberMemberships() {
        return memberMemberships;
    }

    public void setMemberMemberships(Set<MemberMembership> memberMemberships) {
        this.memberMemberships = memberMemberships;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Membership)) {
            return false;
        }
        Membership other = (Membership) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
