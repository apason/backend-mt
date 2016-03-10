package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import fi.helsinki.cs.mobiilitiedekerho.backend.models.SubUser;
import fi.helsinki.cs.mobiilitiedekerho.backend.models.User; //???

import org.sql2o.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;


public class SubUserService {

    private final Sql2o sql2o;

    public SubUserService(Sql2o sql2o) {
        this.sql2o = sql2o;
    }
}
