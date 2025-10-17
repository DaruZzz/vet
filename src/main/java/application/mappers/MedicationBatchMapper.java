package application.mappers;

import application.inputDTO.MedicationBatchCommand;
import application.outputDTO.MedicationBatchInformation;
import domain.MedicationBatch;

public class MedicationBatchMapper {

    public static MedicationBatch commandToDomain(MedicationBatchCommand command) {
        MedicationBatch batch = new MedicationBatch();
        batch.setLotNumber(command.lotNumber());
        batch.setReceivedDate(command.receivedDate());
        batch.setExpiryDate(command.expiryDate());
        batch.setInitialQuantity(command.initialQuantity());
        batch.setCurrentQuantity(command.initialQuantity());
        batch.setPurchasePricePerUnit(command.purchasePricePerUnit());
        batch.setStorageLocation(command.storageLocation());
        return batch;
    }

    public static MedicationBatchInformation toInformation(MedicationBatch batch) {
        return new MedicationBatchInformation(
                batch.getBatchId(),
                batch.getMedication().getName(),
                batch.getLotNumber(),
                batch.getReceivedDate(),
                batch.getExpiryDate(),
                batch.getInitialQuantity(),
                batch.getCurrentQuantity(),
                batch.getPurchasePricePerUnit(),
                batch.getStorageLocation(),
                batch.isExpired(),
                batch.isDepleted()
        );
    }
}
