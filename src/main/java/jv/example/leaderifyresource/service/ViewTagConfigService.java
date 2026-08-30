package jv.example.leaderifyresource.service;

import jakarta.transaction.Transactional;
import jv.example.leaderifyresource.exception.AccessDeniedException;
import jv.example.leaderifyresource.exception.ResourceAlreadyExistsException;
import jv.example.leaderifyresource.exception.ResourceNotFoundException;
import jv.example.leaderifyresource.model.TagCategory;
import jv.example.leaderifyresource.model.View;
import jv.example.leaderifyresource.model.ViewTagConfig;
import jv.example.leaderifyresource.record.request.ViewTagConfigRequest;
import jv.example.leaderifyresource.record.response.ViewTagConfigResponse;
import jv.example.leaderifyresource.repository.TagCategoryRepository;
import jv.example.leaderifyresource.repository.ViewRepository;
import jv.example.leaderifyresource.repository.ViewTagConfigRepository;
import jv.example.leaderifyresource.utils.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ViewTagConfigService {
    private final ViewTagConfigRepository viewTagConfigRepository;
    private final ViewRepository viewRepository;
    private final TagCategoryRepository tagCategoryRepository;

    @Transactional
    public ViewTagConfigResponse createViewTagConfig(ViewTagConfigRequest request, Long viewId, Long tagCategoryId) {
        View view =
                viewRepository.findById(viewId).orElseThrow(() -> new ResourceNotFoundException(
                        View.class.getSimpleName(), viewId
                ));

        Role userRole = view.getUserLeaderboard().getRole();
        if(userRole.equals(Role.VIEWER))
            throw new AccessDeniedException(View.class.getSimpleName());

        TagCategory tagCategory = tagCategoryRepository.findById(tagCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        TagCategory.class.getSimpleName(), tagCategoryId
                ));

        Optional<ViewTagConfig> exists =
                viewTagConfigRepository.findByViewIdAndTagCategoryId(viewId, tagCategoryId);
        if (exists.isPresent()) {
            throw new ResourceAlreadyExistsException(ViewTagConfig.class.getSimpleName());
        }

        ViewTagConfig viewTagConfig = new ViewTagConfig();
        viewTagConfig.setTagCategory(tagCategory);
        viewTagConfig.setView(view);
        viewTagConfig.setFilter(request.filter());
        viewTagConfig.setTarget(request.target());
        ViewTagConfig savedViewTagConfig = viewTagConfigRepository.save(viewTagConfig);

        return new ViewTagConfigResponse(
                savedViewTagConfig.getId(),
                request.filter(),
                request.target()
        );
    }

    public ViewTagConfigResponse findViewTagConfigById(Long id) {
        ViewTagConfig viewTagConfig =
                viewTagConfigRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                        ViewTagConfig.class.getSimpleName(), id
                ));
        return new ViewTagConfigResponse(
                viewTagConfig.getId(),
                viewTagConfig.getFilter(),
                viewTagConfig.getTarget()
        );
    }

    public Page<ViewTagConfigResponse> listViewTagConfigs(Pageable pageable) {
        return viewTagConfigRepository.findAll(pageable)
                .map(viewTagConfig -> new ViewTagConfigResponse(
                        viewTagConfig.getId(),
                        viewTagConfig.getFilter(),
                        viewTagConfig.getTarget()
                ));
    }

    @Transactional
    public ViewTagConfigResponse updateViewTagConfig(Long id, ViewTagConfigRequest request) {
        ViewTagConfig viewTagConfig = viewTagConfigRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ViewTagConfig.class.getSimpleName(), id
                ));

        viewTagConfig.setFilter(request.filter());
        viewTagConfig.setTarget(request.target());

        return new ViewTagConfigResponse(
                viewTagConfig.getId(),
                viewTagConfig.getFilter(),
                viewTagConfig.getTarget()
        );
    }

    @Transactional
    public void deleteViewTagConfig(Long id) {
        ViewTagConfig viewTagConfig = viewTagConfigRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ViewTagConfig.class.getSimpleName(), id
                ));
        viewTagConfigRepository.delete(viewTagConfig);
    }
}
