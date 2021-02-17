package com.udacity.jwdnd.course1.cloudstorage.utility;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.model.UserItems;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public final class Utils {

    private Utils() {
    }

    public static void checkItemExistsAndUserIsAuthorizedOrThrowError(UserItems item, User user) {
        if (item == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Item does not exist."
            );
        };

        if (item.getUserId() != user.getUserId()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "User is not authorized to access that item."
            );
        }
    }
}
