package interfaces;

import java.util.List;

// Interface pour la recherche dans une collection
public interface Searchable<T> {

    // Rechercher des éléments par mot-clé
    List<T> search(String keyword);
}