package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Process;
import fiuba.tpp.reactorapp.entities.TesisFile;
import fiuba.tpp.reactorapp.model.dto.FileTemplateDTO;
import fiuba.tpp.reactorapp.model.exception.FileNotFoundException;
import fiuba.tpp.reactorapp.model.request.TesisFileRequest;
import fiuba.tpp.reactorapp.model.response.TesisFileResponse;
import fiuba.tpp.reactorapp.repository.ProcessRepository;
import fiuba.tpp.reactorapp.repository.TesisFileRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TesisFileService {

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private TesisFileRepository tesisFileRepository;


    public TesisFileResponse uploadFile(TesisFileRequest request) throws IOException {
        TesisFile tesisFile = generateTesisFromRequest(request);
        tesisFileRepository.save(tesisFile);
        return new TesisFileResponse(tesisFile);
    }

    private TesisFile generateTesisFromRequest(TesisFileRequest request) throws IOException {
        TesisFile tesisFile = new TesisFile();
        tesisFile.setName(FilenameUtils.getBaseName(request.getTesis().getOriginalFilename()));
        tesisFile.setData(request.getTesis().getBytes());
        tesisFile.setType(FilenameUtils.getExtension(request.getTesis().getOriginalFilename()));
        tesisFile.setPublicationDate(request.getFechaPublicacion());
        tesisFile.setProcesses(getProcessesFromTesis(request.getProcesosIds()));
        return tesisFile;
    }


    private List<Process> getProcessesFromTesis(String ids){
        if(ids != null && !ids.isEmpty()){
            String[] idsArray = ids.split(",");
            List<Long> processesIds = new ArrayList<>();
            for (String id: idsArray) {
                processesIds.add(Long.valueOf(id));
            }
            return processRepository.getProcessesByIds(processesIds);
        }
        return new ArrayList<>();
    }

    public FileTemplateDTO downloadFile(Long id){
        Optional<TesisFile> tesis = tesisFileRepository.findById(id);
        if(tesis.isPresent()){
            TesisFile file =  tesis.get();
            return new FileTemplateDTO(new ByteArrayResource(file.getData()),buildFileName(file),file.getData().length);
        }
        throw new FileNotFoundException();
    }

    private String buildFileName(TesisFile file){
        return file.getName().concat(".").concat(file.getType());
    }
}
