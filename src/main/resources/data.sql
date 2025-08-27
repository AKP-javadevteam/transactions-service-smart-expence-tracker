-- src/main/resources/data.sql
INSERT INTO transactions
  (id, transaction_id, user_id, date, merchant, amount, currency, category)
VALUES
  ('11111111-1111-1111-1111-111111111111','txn_1','alice','2025-09-01T10:00:00Z','Supermarkt Berlin',-55.45,'EUR','Groceries'),
  ('22222222-2222-2222-2222-222222222222','txn_2','alice','2025-09-02T18:30:00Z','BVG Ticket',-2.90,'EUR','Transport'),
  ('33333333-3333-3333-3333-333333333333','txn_3','alice','2025-09-05T20:00:00Z','Cinema',-15.00,'EUR','Entertainment')
ON CONFLICT (id) DO NOTHING;
