package com.serverApp.serverApp.controllers;

import com.serverApp.serverApp.models.Accounts;
import com.serverApp.serverApp.models.Article;
import com.serverApp.serverApp.other.ArticleRetrieval;
import com.serverApp.serverApp.repositories.AccountsRepository;
import com.serverApp.serverApp.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

@RestController
public class ArticleController {

    private String[] keywords = {"interest rates", "student loans", "saving", "savings account", "CD", "CD rates"};
    private String[] loanKeywords = {"interest rates", "student loans"};
    private String[] savingsAccountKeywords = {"saving", "savings account"};
    private String[] certificateOfDepositKeywords = {"CD", "CD rates"};


    @Autowired
    ArticleRepository articleRepo;

    @Autowired
    AccountsRepository accountRepo;

    @Autowired
    UserController userRepo;

    @RequestMapping("/article/updateDB")
    @Scheduled(fixedRate = 18000000)//5 hours
    public void fetchNewArticles(){
        //API key is b46a1992ed6c457bb31e58178813a3cd

        ArticleRetrieval r = new ArticleRetrieval();
        ArrayList<Article> articles;

        int insertCount = 0;

        for(int i = 0; i < keywords.length; i ++){
            try{
                URL articlesURL = new URL("https://newsapi.org/v2/everything?" +
                    "q=" + URLEncoder.encode(keywords[i], "UTF-8") + "&" +
                    "apiKey=b46a1992ed6c457bb31e58178813a3cd");
                //System.out.println(articlesURL.toString());
                articles = r.getFromURL(articlesURL);

                for(int j = 0; j < articles.size() && j < 5; j++) {
                    articles.get(j).setKeyword(keywords[i]); //set keyword
                    articles.get(j).setIsActive(1); //set to active
                    articles.get(j).setUserId(0);//autogenerated user
                    articles.get(j).setVotes(0);
                    if (articles.get(j).getDescription().length() > 150) {
                        //truncate description if too long
                        articles.get(j).setDescription(articles.get(j).getDescription().substring(0, 147) + "...");
                    }
                    articles.get(j).setDescription(articles.get(j).getDescription().replaceAll("\"", "'"));
                    articles.get(j).setTitle(articles.get(j).getTitle().replaceAll("\"", "'"));
                    if (articleRepo.getDuplicates(articles.get(j).getUrl()) == 0) {
                        articleRepo.save(articles.get(j));
                        insertCount ++;
                    }
                }
                articles = null;
            }catch(Exception e){
                System.out.println("Error in listTitles" + e.getMessage());
            }
        }
        System.out.println("Fetching articles...\n" + insertCount + " articles inserted.");
    }


    @GetMapping("/article/getAll")
    public String getAll(){

        String rString = "{";

        Article[] articles = articleRepo.getAllArticles();
        rString += "\"numArticles\":\"" + articles.length +"\",";

        for(int i = 0; i < articles.length; i ++){
            if(i == articles.length - 1){
                rString =
                        rString
                                + "\"article"
                                + i
                                + "\": {"
                                + "\"id\":\""
                                + articles[i].getId()
                                + "\","
                                + "\"title\":\""
                                + articles[i].getTitle()
                                + "\","
                                + "\"votes\":\""
                                + articles[i].getVotes()
                                + "\","
                                + "\"description\":\""
                                + articles[i].getDescription()
                                + "\","
                                + "\"pictureUrl\":\""
                                + articles[i].getUrlToImage()
                                + "\","
                                + "\"url\":\""
                                + articles[i].getUrl()
                                + "\","
                                + "\"isActive\":\""
                                + articles[i].getIsActive()
                                + "\"}";
            } else {
                rString =
                        rString + "\"article"
                                + i
                                + "\": {"
                                + "\"id\":\""
                                + articles[i].getId()
                                + "\","
                                + "\"title\":\""
                                + articles[i].getTitle()
                                + "\","
                                + "\"votes\":\""
                                + articles[i].getVotes()
                                + "\","
                                + "\"description\":\""
                                + articles[i].getDescription()
                                + "\","
                                + "\"pictureUrl\":\""
                                + articles[i].getUrlToImage()
                                + "\","
                                + "\"url\":\""
                                + articles[i].getUrl()
                                + "\","
                                + "\"isActive\":\""
                                + articles[i].getIsActive()
                                + "\"},";
            }
        }
        rString += "}";
        return rString;
    }

    @GetMapping("/article/adminGetAll")
    public String adminGetAll(){
        String rString = "{";

        Article[] articles = articleRepo.getAllArticles();
        rString += "\"numArticles\":\"" + articles.length +"\", \"articles\": [";

        for(int i = 0; i < articles.length; i ++){
            if(i == articles.length - 1){
                rString =
                        rString
                                + "{"
                                + "\"id\":\""
                                + articles[i].getId()
                                + "\","
                                + "\"title\":\""
                                + articles[i].getTitle()
                                + "\","
                                + "\"votes\":\""
                                + articles[i].getVotes()
                                + "\","
                                + "\"userId\":\""
                                + articles[i].getUserId()
                                + "\","
                                + "\"description\":\""
                                + articles[i].getDescription()
                                + "\","
                                + "\"pictureUrl\":\""
                                + articles[i].getUrlToImage()
                                + "\","
                                + "\"url\":\""
                                + articles[i].getUrl()
                                + "\","
                                + "\"isActive\":\""
                                + articles[i].getIsActive()
                                + "\"}]";
            } else {
                rString =
                        rString + "{"
                                + "\"id\":\""
                                + articles[i].getId()
                                + "\","
                                + "\"title\":\""
                                + articles[i].getTitle()
                                + "\","
                                + "\"votes\":\""
                                + articles[i].getVotes()
                                + "\","
                                + "\"userId\":\""
                                + articles[i].getUserId()
                                + "\","
                                + "\"description\":\""
                                + articles[i].getDescription()
                                + "\","
                                + "\"pictureUrl\":\""
                                + articles[i].getUrlToImage()
                                + "\","
                                + "\"url\":\""
                                + articles[i].getUrl()
                                + "\","
                                + "\"isActive\":\""
                                + articles[i].getIsActive()
                                + "\"},";
            }
        }
        rString += "}";
        return rString;
    }



    @GetMapping("/article/getPersonal/{id}")
    public String getPersonal(@PathVariable long id){
        Accounts[] accounts = accountRepo.getAccountsById(id);
        //System.out.println("Number of accounts: " + accounts.length);
        ArrayList<String> keywords = new ArrayList<>();
        boolean loansRetrieved = false, savingsAccountsRetrieved = false, CDsRetrieved = false;
        for(int i = 0; i < accounts.length; i ++){
            switch(accounts[i].getType()){
                case "Loan":
                    if (!loansRetrieved) {
                        keywords.addAll(Arrays.asList(loanKeywords));
                        loansRetrieved = true;
                    }
                    break;
                case "SavingsAccount":
                    if (!savingsAccountsRetrieved) {
                        keywords.addAll(Arrays.asList(savingsAccountKeywords));
                        savingsAccountsRetrieved = true;
                    }
                        break;
                case "CertificateOfDeposit":
                    if (!CDsRetrieved) {
                        keywords.addAll(Arrays.asList(certificateOfDepositKeywords));
                        CDsRetrieved = true;
                    }
                    break;
                default:
                    //do nothing, account has no type or no keywords
                    break;
            }
        }

        ArrayList<Article> articles = new ArrayList<>();
        for (int i = 0; i < keywords.size(); i++) {
            articles.addAll(Arrays.asList(articleRepo.getAllArticlesWithKeyword(keywords.get(i))));
        }

        String rString = "{";
        if (articles.size() > 0) {
            rString += "\"numArticles\":\"" + articles.size() + "\",";
        }else{
            rString += "\"numArticles\":\"" + articles.size() + "\"";
        }

        for(int i = 0; i < articles.size(); i ++){
            if(i == articles.size() - 1){
                rString =
                        rString
                                + "\"article"
                                + i
                                + "\": {"
                                + "\"id\":\""
                                + articles.get(i).getId()
                                + "\","
                                + "\"title\":\""
                                + articles.get(i).getTitle()
                                + "\","
                                + "\"votes\":\""
                                + articles.get(i).getVotes()
                                + "\","
                                + "\"description\":\""
                                + articles.get(i).getDescription()
                                + "\","
                                + "\"pictureUrl\":\""
                                + articles.get(i).getUrlToImage()
                                + "\","
                                + "\"url\":\""
                                + articles.get(i).getUrl()
                                + "\","
                                + "\"isActive\":\""
                                + articles.get(i).getIsActive()
                                + "\"}";
            } else {
                rString =
                        rString + "\"article"
                                + i
                                + "\": {"
                                + "\"id\":\""
                                + articles.get(i).getId()
                                + "\","
                                + "\"title\":\""
                                + articles.get(i).getTitle()
                                + "\","
                                + "\"votes\":\""
                                + articles.get(i).getVotes()
                                + "\","
                                + "\"description\":\""
                                + articles.get(i).getDescription()
                                + "\","
                                + "\"pictureUrl\":\""
                                + articles.get(i).getUrlToImage()
                                + "\","
                                + "\"url\":\""
                                + articles.get(i).getUrl()
                                + "\","
                                + "\"isActive\":\""
                                + articles.get(i).getIsActive()
                                + "\"},";
            }
        }
        rString += "}";
        return rString;
    }

    @DeleteMapping("/article/archive/{id}")
    public String archiveArticle(@PathVariable int id){
        articleRepo.deleteArticle(id);
        return "{\"message\":\"success\"}";
    }

    @RequestMapping("/article/upvote/{userId}/{articleId}")
    public String upvoteArticle(@PathVariable long userId, @PathVariable long articleId){

    }

}
