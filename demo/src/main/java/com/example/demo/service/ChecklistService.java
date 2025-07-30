package com.example.demo.service;

import com.example.demo.dto.request.ChecklistRequestDTO;
import com.example.demo.dto.response.ChecklistResponseDTO;
import com.example.demo.dto.response.ChecklistDetailResponseDTO;
import com.example.demo.dto.response.ChecklistItemResponseDTO;
import com.example.demo.dto.response.ChecklistProgressDTO;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.checklist.Checklist;
import com.example.demo.model.checklist_items.ChecklistItems;
import com.example.demo.repository.ChecklistRepository;
import com.example.demo.repository.ChecklistItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Checklist işlemleri için Service sınıfı
 *
 * Bu sınıf checklist'ler ile ilgili tüm business logic işlemlerini gerçekleştirir.
 * Repository katmanı ile controller katmanı arasında köprü görevi görür.
 * CardController tarafından kullanılır.
 *
 * @author Nodora Team
 * @version 1.0
 */
@Service
@Transactional
public class ChecklistService {

    private final ChecklistRepository checklistRepository;
    private final ChecklistItemRepository checklistItemRepository;

    /**
     * Constructor injection ile dependency'leri alır
     *
     * @param checklistRepository Checklist repository
     * @param checklistItemRepository ChecklistItem repository
     */
    @Autowired
    public ChecklistService(ChecklistRepository checklistRepository,
                            ChecklistItemRepository checklistItemRepository) {
        this.checklistRepository = checklistRepository;
        this.checklistItemRepository = checklistItemRepository;
    }

    /**
     * Yeni bir checklist oluşturur
     *
     * @param requestDTO Checklist oluşturma isteği
     * @return ChecklistResponseDTO Oluşturulan checklist
     */
    @Transactional
    public ChecklistResponseDTO createChecklist(ChecklistRequestDTO requestDTO) {
        // Position değeri belirtilmemişse en sona ekle
        Integer position = requestDTO.getPosition();
        if (position == null) {
            position = checklistRepository.findMaxPositionByCardId(requestDTO.getCardId())
                    .orElse(0) + 1;
        }

        // Entity oluştur - ID otomatik generate olacak
        Checklist checklist = new Checklist();
        checklist.setTitle(requestDTO.getTitle());
        checklist.setCardId(requestDTO.getCardId());
        checklist.setPosition(position);

        // Kaydet
        Checklist savedChecklist = checklistRepository.save(checklist);

        // DTO'ya dönüştür ve döndür
        return convertToResponseDTO(savedChecklist);
    }

    /**
     * Belirli bir checklist'i ID ile getirir
     *
     * @param checklistId Checklist kimlik numarası
     * @return ChecklistResponseDTO Bulunan checklist
     */
    @Transactional(readOnly = true)
    public ChecklistResponseDTO getChecklistById(Integer checklistId) {
        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new EntityNotFoundException("Checklist", checklistId));

        return convertToResponseDTO(checklist);
    }

    /**
     * Belirli bir checklist'i detayları (items dahil) ile getirir
     *
     * @param checklistId Checklist kimlik numarası
     * @return ChecklistDetailResponseDTO Checklist ve item'ları
     */
    @Transactional(readOnly = true)
    public ChecklistDetailResponseDTO getChecklistWithDetails(Integer checklistId) {
        // Checklist'i getir
        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new EntityNotFoundException("Checklist", checklistId));

        // İlişkili item'ları getir
        List<ChecklistItems> items = checklistItemRepository.findByChecklistIdOrderByPositionAsc(checklistId);

        // DetailResponseDTO oluştur
        ChecklistDetailResponseDTO detailDTO = new ChecklistDetailResponseDTO();
        detailDTO.setChecklistId(checklist.getChecklistId());
        detailDTO.setTitle(checklist.getTitle());
        detailDTO.setCardId(checklist.getCardId());
        detailDTO.setPosition(checklist.getPosition());

        // Item'ları DTO'ya dönüştür
        List<ChecklistItemResponseDTO> itemDTOs = items.stream()
                .map(this::convertItemToResponseDTO)
                .collect(Collectors.toList());
        detailDTO.setItems(itemDTOs);

        return detailDTO;
    }

    /**
     * Belirli bir karta ait tüm checklist'leri getirir
     *
     * @param cardId Kart kimlik numarası
     * @return List<ChecklistResponseDTO> Karta ait checklist'ler
     */
    @Transactional(readOnly = true)
    public List<ChecklistResponseDTO> getChecklistsByCardId(Integer cardId) {
        List<Checklist> checklists = checklistRepository.findByCardIdOrderByPositionAsc(cardId);
        return checklists.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Mevcut bir checklist'i günceller
     *
     * @param checklistId Güncellenecek checklist kimlik numarası
     * @param requestDTO Güncelleme verileri
     * @return ChecklistResponseDTO Güncellenmiş checklist
     */
    public ChecklistResponseDTO updateChecklist(Integer checklistId, ChecklistRequestDTO requestDTO) {
        // Mevcut checklist'i getir
        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new EntityNotFoundException("Checklist", checklistId));

        // Alanları güncelle
        checklist.setTitle(requestDTO.getTitle());
        checklist.setCardId(requestDTO.getCardId());

        if (requestDTO.getPosition() != null) {
            checklist.setPosition(requestDTO.getPosition());
        }

        // Kaydet
        Checklist updatedChecklist = checklistRepository.save(checklist);

        return convertToResponseDTO(updatedChecklist);
    }

    /**
     * Checklist'in position'ını günceller (sürükle-bırak için)
     *
     * @param checklistId Position'ı güncellenecek checklist kimlik numarası
     * @param newPosition Yeni position değeri
     * @return ChecklistResponseDTO Güncellenmiş checklist
     */
    public ChecklistResponseDTO updatePosition(Integer checklistId, Integer newPosition) {
        // Checklist'i getir
        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new EntityNotFoundException("Checklist", checklistId));

        // Position'ı güncelle
        checklist.setPosition(newPosition);

        // Kaydet
        Checklist updatedChecklist = checklistRepository.save(checklist);

        return convertToResponseDTO(updatedChecklist);
    }

    /**
     * Belirli bir checklist'i siler
     *
     * @param checklistId Silinecek checklist kimlik numarası
     */
    public void deleteChecklist(Integer checklistId) {
        // Checklist varlığını kontrol et
        if (!checklistRepository.existsById(checklistId)) {
            throw new EntityNotFoundException("Checklist", checklistId);
        }

        // İlişkili item'ları cascade sil
        checklistItemRepository.deleteByChecklistId(checklistId);

        // Checklist'i sil
        checklistRepository.deleteById(checklistId);
    }

    /**
     * Belirli bir checklist'e ait ilerleme bilgilerini hesaplar
     *
     * @param checklistId İlerleme bilgisi hesaplanacak checklist kimlik numarası
     * @return ChecklistProgressDTO İlerleme bilgileri
     */
    @Transactional(readOnly = true)
    public ChecklistProgressDTO getChecklistProgress(Integer checklistId) {
        // Checklist'i getir
        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new EntityNotFoundException("Checklist", checklistId));

        // Progress DTO oluştur
        ChecklistProgressDTO progress = new ChecklistProgressDTO(checklistId, checklist.getTitle());

        // İstatistikleri hesapla
        long totalItems = checklistItemRepository.countByChecklistId(checklistId);
        long completedItems = checklistItemRepository.countByChecklistIdAndIsCompletedTrue(checklistId);

        progress.calculateProgress(totalItems, completedItems);

        return progress;
    }

    /**
     * Belirli bir checklist'e ait tamamlanmış item'ları siler
     *
     * @param checklistId Tamamlanmış item'ları silinecek checklist kimlik numarası
     */
    public void deleteCompletedItems(Integer checklistId) {
        // Checklist varlığını kontrol et
        if (!checklistRepository.existsById(checklistId)) {
            throw new EntityNotFoundException("Checklist", checklistId);
        }

        // Tamamlanmış item'ları sil
        checklistItemRepository.deleteByChecklistIdAndIsCompletedTrue(checklistId);
    }

    /**
     * Checklist entity'sini ResponseDTO'ya dönüştürür
     *
     * @param checklist Dönüştürülecek checklist entity'si
     * @return ChecklistResponseDTO
     */
    private ChecklistResponseDTO convertToResponseDTO(Checklist checklist) {
        ChecklistResponseDTO dto = new ChecklistResponseDTO();
        dto.setChecklistId(checklist.getChecklistId());
        dto.setTitle(checklist.getTitle());
        dto.setCardId(checklist.getCardId());
        dto.setPosition(checklist.getPosition());
        return dto;
    }

    /**
     * ChecklistItem entity'sini ResponseDTO'ya dönüştürür
     *
     * @param item Dönüştürülecek checklist item entity'si
     * @return ChecklistItemResponseDTO
     */
    private ChecklistItemResponseDTO convertItemToResponseDTO(ChecklistItems item) {
        ChecklistItemResponseDTO dto = new ChecklistItemResponseDTO();
        dto.setChecklistItemsId(item.getChecklistItemsId());
        dto.setChecklistId(item.getChecklistId());
        dto.setText(item.getText());
        dto.setIsCompleted(item.getIsCompleted());
        dto.setPosition(item.getPosition());
        return dto;
}
}