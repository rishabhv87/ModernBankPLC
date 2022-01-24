# ModernBankPLC



### Assumption

1. Currency is mostly pound . There is no logic of currency conversion in code.
2. Since data is non-persistant and is using in-memory data structure , hence the data gets lost on restart
3. Account services and payment services are pre-authorized. No authorization logic is present.
4. Payment url not provided in the challenge , so it is assumed to be /payments/makePayment
5. Each account has only 1 type of balance . Not seperate balance as pending balance or booked balance
6. Few accounts already exists in the system. So, need to add them first before account or payment service.
7. Completed transactions cannot/should not be deleted or updated.So, no api methods exposed for them 
8. Transactions repo is seperate than accounts repo as transactions can grow exponentialy , and it may not be good idea to add transactions to acounts repo.
9. The expected output example for the mini statement in the challenge is not a valid json . Hence , I took the liberty to tweak the output a bit to be returned as valid json.
10. Postman collection file is part of the project. So, please import it to test and verify the application easily.
