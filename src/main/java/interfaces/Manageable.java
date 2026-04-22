package interfaces;

// Interface définissant les opérations de gestion de base
public interface Manageable<T> {

    // Ajouter un élément
    void add(T item);

    // Supprimer un élément
    void remove(T item);

    // Mettre à jour un élément
    void update(T oldItem, T newItem);
}