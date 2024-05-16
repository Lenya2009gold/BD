package bd.bd;

import javafx.collections.ObservableList;

public interface DetailProviderDAO {
    ObservableList<DetailProviderData> loadDetails();
}
