package ar.edu.itba.hci.profitapp.viewModel.repositoryVM;

import androidx.lifecycle.ViewModel;

public class RepositoryViewModel<T> extends ViewModel {
    protected T repository;

    public RepositoryViewModel(T repository) {
        this.repository = repository;
    }
}
