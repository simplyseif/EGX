package org.egx.notifications.services;

import exceptions.ResourceNotFoundException;
import org.egx.notifications.dto.SubscriptionRequest;
import org.egx.notifications.entity.Equity;
import org.egx.notifications.entity.StockSubscription;
import org.egx.notifications.entity.SubscribedUser;
import org.egx.notifications.entity.SubscriptionId;
import org.egx.notifications.repos.EquityRepository;
import org.egx.notifications.repos.StockSubscriptionRepository;
import org.egx.notifications.repos.SubscribedUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockSubscriptionServiceUnitTest {

    @InjectMocks
    private StockSubscriptionService stockSubscriptionService;
    @Mock
    private SubscribedUserRepository subscribedUserRepository;
    @Mock
    private EquityRepository equityRepository;
    @Mock
    private StockSubscriptionRepository subscriptionRepository;

    Equity equity;
    SubscribedUser subscribedUser;
    StockSubscription stockSubscription;

    @BeforeEach
    void setUp() {
        equity = Equity.builder().reutersCode("DCRC").sector("Real Estate")
                .listingDate("Date").name("name").ISN("ISN").id(1)
                .build();
        subscribedUser = SubscribedUser.builder().email("test@gmail.com").name("test").id(1).build();
        stockSubscription = new StockSubscription(new SubscriptionId(1,1));
    }

    @Test
    void testSubscribe_whenUserIsNotExistedAndValidEquity_shouldSaveItAndReturnTrueForTheExistedEquity(){
        //arrange
        doReturn(Optional.empty()).when(subscribedUserRepository).findByEmail(any(String.class));
        doReturn(subscribedUser).when(subscribedUserRepository).save(any(SubscribedUser.class));
        doReturn(Optional.of(equity)).when(equityRepository).findByReutersCode(any(String.class));
        doReturn(Optional.empty()).when(subscriptionRepository).findById(any(SubscriptionId.class));
        doReturn(stockSubscription).when(subscriptionRepository).save(any(StockSubscription.class));

        //act & assert
        Assertions.assertTrue(stockSubscriptionService.subscribe("test@gmail.com","test",
                new SubscriptionRequest("DCRC","news")));
    }
    @Test
    void testSubscribe_whenUserIsExistedAndNotExistedEquity_shouldThrowResourceNotFoundException(){
        //arrange
        doReturn(Optional.of(subscribedUser)).when(subscribedUserRepository).findByEmail(any(String.class));
        doReturn(Optional.empty()).when(equityRepository).findByReutersCode(any(String.class));

        //act & assert
        Assertions.assertThrows(ResourceNotFoundException.class, ()->stockSubscriptionService.subscribe("test@gmail.com","test",
                new SubscriptionRequest("NotExisted","stock")));
    }
    @Test
    void testSubscribe_whenUserIsExistedAndValidEquity_shouldReturnTrueForSubscriptionToGivenEquity(){
        //arrange
        doReturn(Optional.of(subscribedUser)).when(subscribedUserRepository).findByEmail(any(String.class));
        doReturn(Optional.of(equity)).when(equityRepository).findByReutersCode(any(String.class));
        doReturn(Optional.empty()).when(subscriptionRepository).findById(any(SubscriptionId.class));
        doReturn(stockSubscription).when(subscriptionRepository).save(any(StockSubscription.class));

        //act & assert
        Assertions.assertTrue(stockSubscriptionService.subscribe("test@gmail.com","test",
                new SubscriptionRequest("DCRC","stock")));
    }
    @Test
    void testSubscribe_whenUserIsAlreadySubscribedToTheGivenEquity_shouldReturnTrue(){
        //arrange
        doReturn(Optional.of(subscribedUser)).when(subscribedUserRepository).findByEmail(any(String.class));
        doReturn(Optional.of(equity)).when(equityRepository).findByReutersCode(any(String.class));
        doReturn(Optional.of(stockSubscription)).when(subscriptionRepository).findById(any(SubscriptionId.class));

        //act & assert
        Assertions.assertTrue(stockSubscriptionService.subscribe("test@gmail.com","test",
                new SubscriptionRequest("DCRC","stock")));
    }
    @Test
    void testRemoveSubscription_whenUserNotRegisteredToAnyEquity_shouldThrowResourceNotFoundException() {
        doReturn(Optional.empty()).when(subscribedUserRepository).findByEmail(any(String.class));
        Assertions.assertThrows(ResourceNotFoundException.class, ()->
                stockSubscriptionService.removeSubscription("test@gmail.com",
                        new SubscriptionRequest("DCRC","stock")));
    }
    @Test
    void testRemoveSubscription_whenUserProvideNonExistedEquity_shouldThrowResourceNotFoundException() {
        doReturn(Optional.of(subscribedUser)).when(subscribedUserRepository).findByEmail(any(String.class));
        doReturn(Optional.empty()).when(equityRepository).findByReutersCode(any(String.class));
        Assertions.assertThrows(ResourceNotFoundException.class,()->
                stockSubscriptionService.removeSubscription("test@gmail.com",new SubscriptionRequest("NotExisted","stock")));
    }
    @Test
    void testRemoveSubscription_whenUserProvideExistedEquityAndAlreadyRegistered_shouldRaiseNoExceptions(){
        doReturn(Optional.of(subscribedUser)).when(subscribedUserRepository).findByEmail(any(String.class));
        doReturn(Optional.of(equity)).when(equityRepository).findByReutersCode(any(String.class));
        doReturn(Optional.of(stockSubscription)).when(subscriptionRepository).findById(any(SubscriptionId.class));
        doNothing().when(subscriptionRepository).delete(any(StockSubscription.class));
        Assertions.assertDoesNotThrow(()->stockSubscriptionService.removeSubscription("test@gmail.com",new SubscriptionRequest("DCRC","stock")));
    }
}