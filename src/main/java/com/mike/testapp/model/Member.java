/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mike.testapp.model;

import java.util.HashSet;
import java.util.Set;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author Mike-MSI
 */
@Entity
@Table(name = "members")
@PrimaryKeyJoinColumn(name = "user_id")
public class Member extends User {

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<MemberMembership> memberMemberships;
    
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<Checkin> checkins;


    public Member(){
        memberMemberships = new HashSet<>();
        checkins = new HashSet<>();
    }

    public Set<MemberMembership> getMemberMemberships() {
        return memberMemberships;
    }

    public void setMemberMemberships(Set<MemberMembership> memberMemberships) {
        this.memberMemberships = memberMemberships;
    }
    
    public void addMemberMembership(MemberMembership m){
        this.memberMemberships.add(m);
        pcs.firePropertyChange("memberMemberships", m, m);
    }
    
    public Set<Checkin> getCheckins() {
        return checkins;
    }

    public void setCheckins(Set<Checkin> checkins) {
        this.checkins = checkins;
    }


}
