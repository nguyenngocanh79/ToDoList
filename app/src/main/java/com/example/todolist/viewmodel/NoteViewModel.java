package com.example.todolist.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.todolist.database.NoteEntity;
import com.example.todolist.repository.NoteRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository noteRepository;
    private MutableLiveData<List<NoteEntity>> listNote;
    private MutableLiveData<Long> idInsert;
    private MutableLiveData<Integer> idUpdate;
    private MutableLiveData<Boolean> isDeleted;
    private MutableLiveData<Throwable> error;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        noteRepository = NoteRepository.getInstance(getApplication());
        listNote = new MutableLiveData<>();
        idInsert = new MutableLiveData<>();
        idUpdate = new MutableLiveData<>();
        isDeleted = new MutableLiveData<>();
        error = new MutableLiveData<>();
    }

    public void queryGetListNote(){
        noteRepository.getListNote()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(noteEntities -> listNote.setValue(noteEntities));
    }

    public void insertNote(NoteEntity noteEntity){
        noteRepository.insertNote(noteEntity) //Tạo ra Flowable (Observable)  ,ở io thread
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<Long>() { //Bắt đầu truyền về observer (idInsert) main thread
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Long aLong) {
                        idInsert.setValue(aLong);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        error.setValue(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void updateNote(NoteEntity noteEntity){
        noteRepository.updateNote(noteEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<Integer>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Integer aLong) {
                        idUpdate.setValue(aLong);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        error.setValue(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void deletedNote(NoteEntity noteEntity){
        noteRepository.delete(noteEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        isDeleted.setValue(true);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        error.setValue(e);
                    }
                });
    }



    public LiveData<List<NoteEntity>> getListNote(){
        return listNote;
    }
    public LiveData<Long> getIdInsert(){
        return idInsert;
    }

    public LiveData<Integer> getIdUpdate(){
        return idUpdate;
    }

    public LiveData<Boolean> getResultDeleted(){
        return isDeleted;
    }


    public static class NoteViewModelFactory implements ViewModelProvider.Factory {

        @NonNull
        private Application application;

        public NoteViewModelFactory(@NonNull Application application) {
            this.application = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
            if (aClass.isAssignableFrom(NoteViewModel.class)){
                return (T) new NoteViewModel(application);
            }
            throw new IllegalArgumentException("Unable to construct viewmodel");
        }
    }
}