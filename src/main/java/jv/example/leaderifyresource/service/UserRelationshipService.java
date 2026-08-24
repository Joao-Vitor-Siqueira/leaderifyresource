package jv.example.leaderifyresource.service;

import jakarta.transaction.Transactional;
import jv.example.leaderifyresource.exception.ResourceAlreadyExistsException;
import jv.example.leaderifyresource.exception.ResourceNotFoundException;
import jv.example.leaderifyresource.model.User;
import jv.example.leaderifyresource.model.UserRelationship;
import jv.example.leaderifyresource.record.request.UserRelationshipRequest;
import jv.example.leaderifyresource.record.response.UserRelationshipResponse;
import jv.example.leaderifyresource.repository.UserRelationshipRepository;
import jv.example.leaderifyresource.repository.UserRepository;
import jv.example.leaderifyresource.utils.RelationshipStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRelationshipService {
    private final UserRelationshipRepository userRelationshipRepository;
    private final UserRepository userRepository;

    @Transactional
    public UserRelationshipResponse createUserRelationship(UserRelationshipRequest request) {
        Long userHighId = Math.max(request.receiverId(), request.requesterId());
        Long userLowId = Math.min(request.receiverId(), request.requesterId());

        Optional<UserRelationship> exists =
                userRelationshipRepository.findByUserHighIdAndUserLowId(userHighId, userLowId);
        if (exists.isPresent()) {
            throw new ResourceAlreadyExistsException(UserRelationship.class.getSimpleName(), exists.get().getId());
        }

        User userHigh = userRepository.findById(userHighId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                User.class.getSimpleName(), userHighId));

        User userLow = userRepository.findById(userLowId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                User.class.getSimpleName(), userLowId));



        UserRelationship userRelationship = new UserRelationship();
        userRelationship.setUserHigh(userHigh);
        userRelationship.setUserLow(userLow);
        userRelationship.setStatus(RelationshipStatus.PENDING_HIGH_LOW); //change later

        UserRelationship savedUserRelationship = userRelationshipRepository.save(userRelationship);

        return new UserRelationshipResponse(
                savedUserRelationship.getId(),
                userHighId,
                userLowId,
                savedUserRelationship.getStatus()
        );
    }

    public UserRelationshipResponse findUserRelationshipById(Long id) {
        UserRelationship userRelationship = userRelationshipRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                UserRelationship.class.getSimpleName(), id));
        return new UserRelationshipResponse(
                userRelationship.getId(),
                userRelationship.getUserHigh().getId(),
                userRelationship.getUserLow().getId(),
                userRelationship.getStatus()
        );
    }

    public Page<UserRelationshipResponse> listUserRelationships(Pageable pageable) {
        return userRelationshipRepository.findAll(pageable)
                .map(userRelationship -> new UserRelationshipResponse(
                        userRelationship.getId(),
                        userRelationship.getUserHigh().getId(),
                        userRelationship.getUserLow().getId(),
                        userRelationship.getStatus()
                ));
    }

    @Transactional
    public UserRelationshipResponse updateUserRelationship(Long id, UserRelationshipRequest request) {
        UserRelationship userRelationship = userRelationshipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        UserRelationship.class.getSimpleName(), id
                ));

        userRelationship.setStatus(RelationshipStatus.FRIENDS); //change later

        return new UserRelationshipResponse(
                userRelationship.getId(),
                userRelationship.getUserHigh().getId(),
                userRelationship.getUserLow().getId(),
                userRelationship.getStatus()
        );
    }

    @Transactional
    public void deleteUserRelationship(Long id) {
        UserRelationship userRelationship = userRelationshipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        UserRelationship.class.getSimpleName(), id
                ));
        userRelationshipRepository.delete(userRelationship);
    }

}
