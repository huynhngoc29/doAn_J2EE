package nhom02.doanmon.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import nhom02.doanmon.entity.Cake;
import nhom02.doanmon.repository.CakeRepository;

@Service
public class CakeService {
    @Autowired
    private CakeRepository cakeRepository;

    public List<Cake> findAll() {
        return cakeRepository.findAll();
    }

    public Optional<Cake> findById(Long id) {
        return cakeRepository.findById(id);
    }

    public Cake save(Cake cake) {
        return cakeRepository.save(cake);
    }

    public void deleteById(Long id) {
        cakeRepository.deleteById(id);
    }
}
