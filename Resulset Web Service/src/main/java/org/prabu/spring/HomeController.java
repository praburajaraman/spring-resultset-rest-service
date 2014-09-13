package org.prabu.spring;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles requests for the application home page.
 * This class is from Spring samples for Spring Web guide
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	@RequestMapping(value={"/rs"},method={RequestMethod.GET},
    		produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody SqlRowSet getRs() throws Exception {
    	
    	
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.h2.Driver.class);
        dataSource.setUsername("sa");
        dataSource.setUrl("jdbc:h2:mem");
        dataSource.setPassword("");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        System.out.println("Creating tables");
        jdbcTemplate.execute("drop table customers if exists");
        jdbcTemplate.execute("create table customers(" +
                "id serial, first_name varchar(255), last_name varchar(255),dob date)");

        String[] names = "John Woo;Jeff Dean;Josh Bloch;Josh Long".split(";");
        for (String fullname : names) {
            String[] name = fullname.split(" ");
            System.out.printf("Inserting customer record for %s %s\n", name[0], name[1]);
            jdbcTemplate.update(
                    "INSERT INTO customers(first_name,last_name,dob) values(?,?,?)",
                    name[0], name[1],new Date());
        }

        System.out.println("Querying for customer records");
        
        /*
         * column label is serialized as Json Name, so we are explicitly mentioning it in "",
         * this way we can map it client side with a POJO
         * first_name as \"firstName\"
         * 
         * Result : 
         * [{"id":1,"firstName":"John","lastName":"Woo","dob":1410494400000},
         * {"id":2,"firstName":"Jeff","lastName":"Dean","dob":1410494400000},
         * {"id":3,"firstName":"Josh","lastName":"Bloch","dob":1410494400000},
         * {"id":4,"firstName":"Josh","lastName":"Long","dob":1410494400000}]
         * 
         * */
        
        return jdbcTemplate.queryForRowSet(
                "select id as \"id\", first_name as \"firstName\", "
                + "last_name as \"lastName\", dob as \"dob\"  from customers");

    	
    }
	
}
