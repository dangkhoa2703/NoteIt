package com.noteit.dto;

import com.noteit.entity.User;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;


@Getter
public class ShareUserRepresentation extends RepresentationModel<ShareUserRepresentation> {

    private final int id;
    private final String username;

    public ShareUserRepresentation (User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }

}
