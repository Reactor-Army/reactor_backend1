package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.TesisFile;

import java.util.List;

public interface TesisFileRepositoryCustom {

    List<TesisFile> getAll(String name);

}
