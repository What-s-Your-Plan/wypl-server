package com.butter.wypl.group.domain;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class MemberGroupId implements Serializable {

    private int member;

    private int group;

}
