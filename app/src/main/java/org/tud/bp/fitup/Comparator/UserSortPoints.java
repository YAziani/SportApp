package com.tud.bp.fitup.Comparator;

import com.tud.bp.fitup.BusinessLayer.User;

import java.util.Comparator;

/**
 * Created by Basti on 22.03.2017.
 */

public class UserSortPoints implements Comparator<User> {

    @Override
    public int compare(User user1, User user2) {
        return Integer.compare(user2.getPoints(), user1.getPoints());
    }
}
