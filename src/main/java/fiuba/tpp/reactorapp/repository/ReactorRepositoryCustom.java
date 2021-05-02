package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.Reactor;
import fiuba.tpp.reactorapp.model.filter.ReactorFilter;

import java.util.List;

public interface ReactorRepositoryCustom {

    List<Reactor> getAll(ReactorFilter filter);
}
