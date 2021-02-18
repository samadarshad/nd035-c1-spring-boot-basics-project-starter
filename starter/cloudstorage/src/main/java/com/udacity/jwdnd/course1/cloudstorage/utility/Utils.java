package com.udacity.jwdnd.course1.cloudstorage.utility;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.model.UserItems;
import com.udacity.jwdnd.course1.cloudstorage.services.CrudService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

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

    public static List<CrudService> genCrudServices(CrudService... crudServices) {
        List<CrudService> list = new ArrayList<>();
        for (CrudService service : crudServices) {
            list.add(service);
        }
        return list;
    }

    public static CrudService itemToServiceMapper(UserItems item, List<CrudService> crudServices) {
        for (CrudService service : crudServices) {
            if (item.getClass() == service.getObjectType()) {
                return service;
            }
        }
        throw new IllegalStateException("Unexpected value: " + item.getClass());
    }

    public static void addItemsToUser(User user, List<CrudService> crudServices, UserItems... items) {
        for (UserItems item : items) {
            item.setUserId(user.getUserId());
            CrudService crudService = itemToServiceMapper(item, crudServices);
            crudService.createAndUpdateObject(item);
        }
    }
}
