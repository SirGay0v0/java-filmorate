package ru.yandex.practicum.filmorate.service.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Slf4j
@Service
public class MpaServiceImpl implements MpaService {

    private final MpaStorage mpaStorage;


    @Autowired
    public MpaServiceImpl(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @Override
    public Mpa getMpaById(Integer mpaId) throws NotFoundException {
        Mpa mpa = mpaStorage.getMpaById(mpaId);
        if (mpa != null) {
            log.info("Возвращен mpa c id " + mpaId);
            return mpa;
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public List<Mpa> getAllMpa() {
        log.info("Возвращен список mpa.");
        return mpaStorage.getAllMpa();
    }
}
