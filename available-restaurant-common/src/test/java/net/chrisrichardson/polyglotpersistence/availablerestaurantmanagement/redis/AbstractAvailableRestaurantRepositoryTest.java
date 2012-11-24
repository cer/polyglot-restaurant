package net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.redis;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.AvailableRestaurant;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.SimpleAvailableRestaurantRepository;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.testutil.DatabaseInitializer;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.testutil.IdentityAssigner;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.Restaurant;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.RestaurantMother;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.RestaurantTestData;
import net.chrisrichardson.polyglotpersistence.util.Address;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractAvailableRestaurantRepositoryTest {

    @Autowired
    private SimpleAvailableRestaurantRepository repository;

    @Autowired
    private DatabaseInitializer databaseInitializer;

    private Restaurant ajantaRestaurant;
    private Restaurant eggshopRestaurant;

    private IdentityAssigner identityAssigner = new IdentityAssigner();

    private Restaurant lateNightSnack;

    @Before
    public void setUp() throws Exception {
        ajantaRestaurant = RestaurantMother.makeRestaurant();
        eggshopRestaurant = RestaurantMother.makeEggShopRestaurant();
        lateNightSnack = RestaurantMother.makeLateNightTacos();

        initializeDatabase();

        identityAssigner.assignIdentities(ajantaRestaurant, eggshopRestaurant, lateNightSnack);
        repository.add(ajantaRestaurant);
        repository.add(eggshopRestaurant);
        repository.add(lateNightSnack);
    }

    private void initializeDatabase() {
        databaseInitializer.initialize();
    }

    @Test
    public void testFindAvailableRestaurants() {

        // dumpAvailableRestaurants();

        Date deliveryTime = RestaurantTestData.makeGoodDeliveryTime();
        Address deliveryAddress = RestaurantTestData.getADDRESS1();
        List<AvailableRestaurant> results = repository.findAvailableRestaurants(deliveryAddress, deliveryTime);
        assertFoundAjanta(results);
    }

    @Test
    public void testFindAvailableRestaurants_Monday8Am() {
        Date deliveryTime = RestaurantTestData.makeDeliveryTime(Calendar.MONDAY, 8, 0);
        Address deliveryAddress = RestaurantTestData.getADDRESS1();
        List<AvailableRestaurant> results = repository.findAvailableRestaurants(deliveryAddress, deliveryTime);
        assertFoundRestaurant(RestaurantMother.MONTCLAIR_EGGSHOP, results);
    }

    @Test
    public void testFindAvailableRestaurants_Saturday245pm() {
        Date deliveryTime = RestaurantTestData.makeDeliveryTime(Calendar.SATURDAY, 14, 45);
        Address deliveryAddress = RestaurantTestData.getADDRESS1();
        List<AvailableRestaurant> results = repository.findAvailableRestaurants(deliveryAddress, deliveryTime);
        assertFoundRestaurant(RestaurantMother.MONTCLAIR_EGGSHOP, results);
    }

    @Test
    public void testFindAvailableRestaurants_TuesdayNoon() {
        Date deliveryTime = RestaurantTestData.makeDeliveryTime(Calendar.TUESDAY, 12, 0);
        Address deliveryAddress = RestaurantTestData.getADDRESS1();
        List<AvailableRestaurant> results = repository.findAvailableRestaurants(deliveryAddress, deliveryTime);
        assertFoundRestaurants(results, RestaurantMother.MONTCLAIR_EGGSHOP, "Ajanta");
    }

    private void assertFoundRestaurants(List<AvailableRestaurant> results, String... expectedNames) {
        Set<String> actualNames = new HashSet<String>();
        for (AvailableRestaurant r : results) {
            actualNames.add(r.getName());
        }
        Assert.assertEquals(new HashSet<String>(Arrays.asList(expectedNames)), actualNames);

    }

    @Test
    public void testFindAvailableRestaurants_None() {

        // dumpAvailableRestaurants();

        Date deliveryTime = RestaurantTestData.makeBadDeliveryTime();
        Address deliveryAddress = RestaurantTestData.getADDRESS1();
        List<AvailableRestaurant> results = repository.findAvailableRestaurants(deliveryAddress, deliveryTime);
        Assert.assertTrue(results.isEmpty());
    }

    private void assertFoundAjanta(List<AvailableRestaurant> results) {
        assertFoundRestaurant("Ajanta", results);
    }

    private void assertFoundRestaurant(String expectedName, List<AvailableRestaurant> results) {
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(expectedName, results.iterator().next().getName());
    }

    @Test
    public void testUpdateRestaurant() {
        repository.delete(ajantaRestaurant);
        updateRestaurantOpeningHours();
        repository.add(ajantaRestaurant);

        Date deliveryTime = RestaurantTestData.getTimeTomorrow(RestaurantMother.OPENING_HOUR, RestaurantMother.OPENING_MINUTE - 1);
        Address deliveryAddress = RestaurantTestData.getADDRESS1();
        List<AvailableRestaurant> results = repository.findAvailableRestaurants(deliveryAddress, deliveryTime);
        assertFoundAjanta(results);
    }

    @Test
    public void testUpdateRestaurant2() {
        repository.delete(ajantaRestaurant);

        ajantaRestaurant.setOpeningHours(RestaurantMother.makeOpeningHours(20, 01));

        System.out.println("**** Updating");
        repository.add(ajantaRestaurant);

        Date deliveryTime2 = RestaurantTestData.makeGoodDeliveryTime();
        Address deliveryAddress2 = RestaurantTestData.getADDRESS1();
        List<AvailableRestaurant> results2 = repository.findAvailableRestaurants(deliveryAddress2, deliveryTime2);
        assertEmpty(results2);

    }

    private void assertEmpty(Collection<?> results2) {
        Assert.assertTrue("Should be empty: " + results2, results2.isEmpty());
    }

    private void updateRestaurantOpeningHours() {
        ajantaRestaurant.setOpeningHours(RestaurantMother.makeOpeningHours(RestaurantMother.OPENING_MINUTE - 1));
    }

    @Test
    public void testFindByRestaurantDetails() {
        Restaurant r2 = repository.findDetailsById(ajantaRestaurant.getId());
        assertRestaurantEquals(ajantaRestaurant, r2);
    }

    private void assertRestaurantEquals(Restaurant expectedRestaurant, Restaurant actualRestaurant) {
        Assert.assertNotNull(actualRestaurant);
        Assert.assertEquals(expectedRestaurant.getId(), actualRestaurant.getId());
        Assert.assertEquals(expectedRestaurant.getName(), actualRestaurant.getName());
        Assert.assertEquals(expectedRestaurant.getServiceArea(), actualRestaurant.getServiceArea());
        Assert.assertNotNull(actualRestaurant.getOpeningHours());
        Assert.assertEquals(expectedRestaurant.getOpeningHours().size(), actualRestaurant.getOpeningHours().size());
        Assert.assertNotNull(actualRestaurant.getMenuItems());
        Assert.assertEquals(expectedRestaurant.getMenuItems().size(), actualRestaurant.getMenuItems().size());
    }


}