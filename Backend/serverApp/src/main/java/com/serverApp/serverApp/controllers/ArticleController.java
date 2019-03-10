package com.serverApp.serverApp.controllers;

import com.google.gson.Gson;
import com.serverApp.serverApp.models.Accounts;
import com.serverApp.serverApp.models.Article;
import com.serverApp.serverApp.models.User;
import com.serverApp.serverApp.other.ArticleRetrieval;
import com.serverApp.serverApp.repositories.AccountsRepository;
import com.serverApp.serverApp.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
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

    @RequestMapping("/article")
    public String article(@RequestBody Article article) {
        System.out.println("Revieved article object");
        return "";
    }

    @RequestMapping("/article/updateDB")
    public String listTitles(){
        //API key is b46a1992ed6c457bb31e58178813a3cd
        ArticleRetrieval r = new ArticleRetrieval();
        ArrayList<Article> articles;
        Gson g = new Gson();
        for(int i = 0; i < keywords.length; i ++){
            try{
                URL articlesURL = new URL("https://newsapi.org/v2/everything?" +
                    "q=" + URLEncoder.encode(keywords[i], "UTF-8") + "&" +
                    "apiKey=b46a1992ed6c457bb31e58178813a3cd");
                articles = r.getFromURL(articlesURL);

               /* for(int j = 0; j < articles.size() && j < 5; j ++){
                    articles.get(j).setKeyword(keywords[i]); //set keyword
                    System.out.println(articles.get(j).getKeyword() + " " + j + ": " + articles.get(j).getTitle() + "\n     " + articles.get(j).getDescription() + "\n");
                }*/

                for(int j = 0; j < articles.size() && j < 5; j++) {
                    articles.get(j).setKeyword(keywords[i]); //set keyword
                    articles.get(j).setIsActive(1); //set to active
                    articles.get(j).setUserId(0);//autogenerated user
                    if (articles.get(j).getDescription().length() > 150) {
                        articles.get(j).setDescription(articles.get(j).getDescription().substring(0, 147) + "...");
                    }
                    if (articleRepo.getDuplicates(articles.get(j).getTitle()) == 0) {
                        articleRepo.save(articles.get(j));
                    }
                }
                articles = null;
            }catch(Exception e){
                System.out.println("Error in listTitles" + e.getMessage());
            }
        }
        return "finished";
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
        System.out.println("Number of accounts: " + accounts.length);
        System.out.println("Type: " + accounts[0].getType());
        ArrayList<String> keywords = new ArrayList<>();
        for(int i = 0; i < accounts.length; i ++){
            switch(accounts[i].getType()){
                case "Loan":
                    keywords.addAll(Arrays.asList(loanKeywords));
                    break;
                case "SavingsAccount":
                    keywords.addAll(Arrays.asList(savingsAccountKeywords));
                    break;
                case "CertificateOfDeposit":
                    keywords.addAll(Arrays.asList(certificateOfDepositKeywords));
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
        rString += "\"numArticles\":\"" + articles.size() +"\",";

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
}
