package com.example.demo.service;

import com.example.demo.dto.request.ChecklistItemRequestDTO;
import com.example.demo.dto.response.ChecklistItemResponseDTO;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.checklist_items.ChecklistItems;
import com.example.demo.repository.ChecklistRepository;
import com.example.demo.repository.ChecklistItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ChecklistItem işlemleri için Service sınıfı
 *
 * Bu sınıf checklist item'ları ile ilgili tüm business logic işlemlerini gerçekleştirir.
 * Repository katmanı ile controller katmanı arasında köprü görevi görür.
 * CardController tarafından kullanılır.
 *
 * @author Nodora Team
 * @version 1.0
 */
@Service
@Transactional
public class ChecklistItemService {

    private final ChecklistItemRepository checklistItemRepository;
    private final ChecklistRepository checklistRepository;

    /**
     * Constructor injection ile dependency'leri alır
     *
     * @param checklistItemRepository ChecklistItem repository
     * @param checklistRepository Checklist repository
     */
    @Autowired
    public ChecklistItemService(ChecklistItemRepository checklistItemRepository,
                                ChecklistRepository checklistRepository) {
        this.checklistItemRepository = checklistItemRepository;
        this.checklistRepository = checklistRepository;
    }

    /**
     * Yeni bir checklist item oluşturur
     *
     * @param requestDTO Item oluşturma isteği
     * @return ChecklistItemResponseDTO Oluşturulan item
     */
    public ChecklistItemResponseDTO createItem(ChecklistItemRequestDTO requestDTO) {
        // Checklist varlığını kontrol et
        if (!checklistRepository.existsById(requestDTO.getChecklistId())) {
            throw new EntityNotFoundException("Checklist", requestDTO.getChecklistId());
        }

        // Position değeri belirtilmemişse en sona ekle
        Integer position = requestDTO.getPosition();
        if (position == null) {
            position = checklistItemRepository.findMaxPositionByChecklistId(requestDTO.getChecklistId())
                    .orElse(0) + 1;
        }

        // Entity oluştur
        ChecklistItems item = new ChecklistItems();
        //item.setChecklistItemsId(newId); // Manuel ID set et
        item.setChecklistId(requestDTO.getChecklistId());
        item.setText(requestDTO.getText());
        item.setIsCompleted(requestDTO.getIsCompleted() != null ? requestDTO.getIsCompleted() : false);
        item.setPosition(position);

        // Kaydet
        ChecklistItems savedItem = checklistItemRepository.save(item);

        // DTO'ya dönüştür ve döndür
        return convertToResponseDTO(savedItem);
    }

    /**
     * Belirli bir checklist item'ını ID ile getirir
     *
     * @param itemId Item kimlik numarası
     * @return ChecklistItemResponseDTO Bulunan item
     */
    @Transactional(readOnly = true)
    public ChecklistItemResponseDTO getItemById(Integer itemId) {
        ChecklistItems item = checklistItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("ChecklistItem", itemId));

        return convertToResponseDTO(item);
    }

    /**
     * Belirli bir checklist'e ait tüm item'ları getirir
     *
     * @param checklistId Checklist kimlik numarası
     * @return List<ChecklistItemResponseDTO> Checklist'e ait item'lar (position sıralı)
     */
    @Transactional(readOnly = true)
    public List<ChecklistItemResponseDTO> getItemsByChecklistId(Integer checklistId) {
        // Checklist varlığını kontrol et
        if (!checklistRepository.existsById(checklistId)) {
            throw new EntityNotFoundException("Checklist", checklistId);
        }

        List<ChecklistItems> items = checklistItemRepository.findByChecklistIdOrderByPositionAsc(checklistId);
        return items.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Mevcut bir checklist item'ını günceller
     *
     * @param itemId Güncellenecek item kimlik numarası
     * @param requestDTO Güncelleme verileri
     * @return ChecklistItemResponseDTO Güncellenmiş item
     */
    public ChecklistItemResponseDTO updateItem(Integer itemId, ChecklistItemRequestDTO requestDTO) {
        // Item varlığını kontrol et
        ChecklistItems item = checklistItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("ChecklistItem", itemId));

        // Checklist varlığını kontrol et (yeni checklist'e taşınıyorsa)
        if (!checklistRepository.existsById(requestDTO.getChecklistId())) {
            throw new EntityNotFoundException("Checklist", requestDTO.getChecklistId());
        }

        // Alanları güncelle
        item.setChecklistId(requestDTO.getChecklistId());
        item.setText(requestDTO.getText());
        item.setIsCompleted(requestDTO.getIsCompleted());

        if (requestDTO.getPosition() != null) {
            item.setPosition(requestDTO.getPosition());
        }

        // Kaydet
        ChecklistItems updatedItem = checklistItemRepository.save(item);

        return convertToResponseDTO(updatedItem);
    }

    /**
     * Checklist item'ının tamamlanma durumunu değiştirir (toggle)
     *
     * @param itemId Durumu değiştirilecek item kimlik numarası
     * @return ChecklistItemResponseDTO Güncellenmiş item
     */
    public ChecklistItemResponseDTO toggleCompletion(Integer itemId) {
        // Item varlığını kontrol et
        if (!checklistItemRepository.existsById(itemId)) {
            throw new EntityNotFoundException("ChecklistItem", itemId);
        }

        // Repository metodunu kullanarak toggle yap
        int updatedRows = checklistItemRepository.toggleCompletion(itemId);

        if (updatedRows == 0) {
            throw new EntityNotFoundException("ChecklistItem", itemId);
        }

        // Güncellenmiş item'ı getir ve döndür
        ChecklistItems updatedItem = checklistItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("ChecklistItem", itemId));

        return convertToResponseDTO(updatedItem);
    }

    /**
     * Checklist item'ının sadece text içeriğini günceller
     *
     * @param itemId Text'i güncellenecek item kimlik numarası
     * @param newText Yeni text içeriği
     * @return ChecklistItemResponseDTO Güncellenmiş item
     */
    public ChecklistItemResponseDTO updateText(Integer itemId, String newText) {
        // Input validasyonu
        if (newText == null || newText.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }
        if (newText.length() > 25) {
            throw new IllegalArgumentException("Text cannot be longer than 25 characters");
        }

        // Item'ı getir
        ChecklistItems item = checklistItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("ChecklistItem", itemId));

        // Text'i güncelle
        item.setText(newText.trim());

        // Kaydet
        ChecklistItems updatedItem = checklistItemRepository.save(item);

        return convertToResponseDTO(updatedItem);
    }

    /**
     * Checklist item'ının position'ını günceller (sürükle-bırak için)
     *
     * @param itemId Position'ı güncellenecek item kimlik numarası
     * @param newPosition Yeni position değeri
     * @return ChecklistItemResponseDTO Güncellenmiş item
     */
    public ChecklistItemResponseDTO updatePosition(Integer itemId, Integer newPosition) {
        // Item'ı getir
        ChecklistItems item = checklistItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("ChecklistItem", itemId));

        // Position'ı güncelle
        item.setPosition(newPosition);

        // Kaydet
        ChecklistItems updatedItem = checklistItemRepository.save(item);

        return convertToResponseDTO(updatedItem);
    }

    /**
     * Belirli bir checklist item'ını siler
     *
     * @param itemId Silinecek item kimlik numarası
     */
    public void deleteItem(Integer itemId) {
        // Item varlığını kontrol et
        if (!checklistItemRepository.existsById(itemId)) {
            throw new EntityNotFoundException("ChecklistItem", itemId);
        }

        // Item'ı sil
        checklistItemRepository.deleteById(itemId);
    }

    /**
     * Belirli bir checklist'e ait tamamlanmış tüm item'ları siler
     *
     * @param checklistId Tamamlanmış item'ları silinecek checklist kimlik numarası
     */
    public void deleteCompletedItemsByChecklistId(Integer checklistId) {
        // Checklist varlığını kontrol et
        if (!checklistRepository.existsById(checklistId)) {
            throw new EntityNotFoundException("Checklist", checklistId);
        }

        // Tamamlanmış item'ları sil
        checklistItemRepository.deleteByChecklistIdAndIsCompletedTrue(checklistId);
    }

    /**
     * ChecklistItem entity'sini ResponseDTO'ya dönüştürür
     *
     * @param item Dönüştürülecek checklist item entity'si
     * @return ChecklistItemResponseDTO
     */
    private ChecklistItemResponseDTO convertToResponseDTO(ChecklistItems item) {
        ChecklistItemResponseDTO dto = new ChecklistItemResponseDTO();
        dto.setChecklistItemsId(item.getChecklistItemsId());
        dto.setChecklistId(item.getChecklistId());
        dto.setText(item.getText());
        dto.setIsCompleted(item.getIsCompleted());
        dto.setPosition(item.getPosition());
        return dto;
}
}
