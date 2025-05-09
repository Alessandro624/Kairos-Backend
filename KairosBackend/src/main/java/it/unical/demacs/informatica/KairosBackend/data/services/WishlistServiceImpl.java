package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.data.entities.Event;
import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import it.unical.demacs.informatica.KairosBackend.data.entities.Wishlist;
import it.unical.demacs.informatica.KairosBackend.data.repository.EventRepository;
import it.unical.demacs.informatica.KairosBackend.data.repository.UserRepository;
import it.unical.demacs.informatica.KairosBackend.data.repository.WishlistRepository;
import it.unical.demacs.informatica.KairosBackend.data.repository.specifications.WishlistSpecifications;
import it.unical.demacs.informatica.KairosBackend.dto.wishlist.WishlistFilterDTO;
import it.unical.demacs.informatica.KairosBackend.dto.wishlist.*;
import it.unical.demacs.informatica.KairosBackend.exception.ResourceAlreadyExistsException;
import it.unical.demacs.informatica.KairosBackend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;


    @Override
    public WishlistDTO getWishlistById(UUID id) {
        Wishlist wishlist = wishlistRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Wishlist having id " +id + " doesn't exist"));

        return modelMapper.map(wishlist, WishlistDTO.class);
    }

    @Override
    public WishlistDTO createWishlist(WishlistCreateDTO wishlistCreateDTO) {
        if(wishlistRepository.existsByCreator_IdAndName(wishlistCreateDTO.getCreator(), wishlistCreateDTO.getName()))
            throw new ResourceAlreadyExistsException("Wishlist " + wishlistCreateDTO.getName() + " already exists");

        Wishlist wishlist = modelMapper.map(wishlistCreateDTO, Wishlist.class);

        Wishlist savedWishlist = wishlistRepository.save(wishlist);

        return modelMapper.map(savedWishlist, WishlistDTO.class);
    }

    @Override
    @Transactional
    public void deleteWishlist(UUID wishlistId) {
        Wishlist wishlist = wishlistRepository.findById(wishlistId).orElseThrow(() -> new ResourceNotFoundException("Wishlist " + wishlistId + " doesn't exist"));

        //deletion of all many to many mappings.

        //TODO how to handle deletion of shared wishlists?
        //for now, everything will be deleted.
        wishlist.getSharedUsers().clear();
        wishlist.getWishedEvents().clear();
        wishlistRepository.delete(wishlist);
    }

    //maybe not used for manyToMany columns.
    @Override
    @Transactional
    public WishlistDTO updateWishlist(WishlistUpdateDTO wishlistUpdateDTO, UUID creatorId) {
        Wishlist wishlist = wishlistRepository.findById(wishlistUpdateDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("Wishlist " + wishlistUpdateDTO.getId() + " doesn't exist"));

        if(wishlistRepository.existsByCreator_IdAndName(creatorId, wishlistUpdateDTO.getName()))
            //TODO frontend should do a check (maybe a warning)
            wishlist.setName(wishlistUpdateDTO.getName()+"1");
        else
            wishlist.setName(wishlistUpdateDTO.getName());
        wishlist.setScope(wishlistUpdateDTO.getScope());
        wishlist.setSharedUsers(wishlist.getSharedUsers());
        wishlist.setWishedEvents(wishlist.getWishedEvents());

        Wishlist savedWishlist = wishlistRepository.save(wishlist);

        return modelMapper.map(savedWishlist, WishlistDTO.class);
    }

    @Override
    public Boolean wishlistAlreadyExists(UUID creatorID, String name) {
        return wishlistRepository.existsByCreator_IdAndName(creatorID, name);
    }

    @Override
    public WishlistDTO addUserToWishlist(UUID wishlistId, UUID userId) {
        Wishlist wishlist = wishlistRepository.findById(wishlistId).orElseThrow(() -> new ResourceNotFoundException("Wishlist " + wishlistId + " doesn't exist"));

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User " + userId + " doesn't exist"));

        if(wishlist.getSharedUsers().contains(user)) throw new ResourceAlreadyExistsException("Wishlist "+ wishlistId+" is already shared to User" + userId);

        wishlist.getSharedUsers().add(user);

        //TODO add email, or some notification system to notify added user.
        Wishlist saved =  wishlistRepository.save(wishlist);

        return modelMapper.map(saved, WishlistDTO.class);
    }

    @Override
    public void removeUserFromWishlist(UUID wishlistId, UUID userId) {
        Wishlist wishlist = wishlistRepository.findById(wishlistId).orElseThrow(() -> new ResourceNotFoundException("Wishlist " + wishlistId + " doesn't exist"));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User " + userId + " doesn't exist"));

        if(wishlist.getSharedUsers().contains(user)) throw new ResourceNotFoundException("No User " + userId + "found in Wishlist" + wishlistId);
        wishlist.getSharedUsers().remove(user);

        //TODO add email, or some notification system to notify removed user.
        wishlistRepository.save(wishlist);
    }

    @Override
    public WishlistDTO addEventToWishlist(UUID wishlistId, UUID eventId) {
        Wishlist wishlist = wishlistRepository.findById(wishlistId).orElseThrow(() -> new ResourceNotFoundException("Wishlist " + wishlistId + " doesn't exist"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ResourceNotFoundException("Event " + eventId + " doesn't exist"));

        if(wishlist.getWishedEvents().contains(event)) throw new ResourceAlreadyExistsException("Wishlist "+ wishlistId+" is already shared to Event" + eventId);

        wishlist.getWishedEvents().add(event);

        //TODO add email, or some notification system to notify shared users if wishlist is shared.

        Wishlist saved =  wishlistRepository.save(wishlist);
        return modelMapper.map(saved, WishlistDTO.class);
    }


    @Override
    public void removeEventFromWishlist(UUID wishlistId, UUID eventId) {
        Wishlist wishlist = wishlistRepository.findById(wishlistId).orElseThrow(() -> new ResourceNotFoundException("Wishlist " + wishlistId + " doesn't exist"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ResourceNotFoundException("Event " + eventId + " doesn't exist"));

        if(!wishlist.getWishedEvents().contains(event)) throw new ResourceNotFoundException("No Event " + eventId + "found in Wishlist" + wishlistId);

        wishlist.getWishedEvents().remove(event);
        //TODO add email, or some notification system to notify shared users if wishlist is shared.
        wishlistRepository.save(wishlist);
    }

    @Override
    public int countWishlistsByCreator(UUID creatorId) {
        return wishlistRepository.countByCreator_Id(creatorId);
    }

    @Override
    public Page<WishlistDTO> getWishlists(WishlistFilterDTO wishlistFilterDTO, Integer page, Integer size) {
        //TODO add sorting
        //PageRequest pageRequest = PageRequest.of(page, size, sorting...);

        //FIXME where to add specification? Here or in WishlistController?
        //in the example is passed as a parameter of service function!
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Wishlist> wishlistsPage = wishlistRepository.findAll(WishlistSpecifications.filterWishlists(wishlistFilterDTO),pageRequest);

        return wishlistsPage.map(wishlist -> modelMapper.map(wishlist,WishlistDTO.class));
    }
}
