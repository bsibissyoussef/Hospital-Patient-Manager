package interfaces;

import java.util.List;

// Interface pour afficher une liste d'éléments
public interface Displayable<T> {

    // Retourner la liste de tous les éléments à afficher
    List<T> display();
}