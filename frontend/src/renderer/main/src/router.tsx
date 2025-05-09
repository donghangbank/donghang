import { createHashRouter } from "react-router-dom";
import App from "./pages/App";
import DepositConfirmPage from "./pages/general/deposit/DepositConfirmPage";
import MainLayout from "./layouts/MainLayout";
import { NotFoundPage } from "./pages/common/NotFoundPage";
import DepositOptionPage from "./pages/general/deposit/DepositOptionPage";
import DepositCashInputPage from "./pages/general/deposit/DepositCashInputPage";
import DepositCashCountingPage from "./pages/general/deposit/DepositCashCountingPage";
import DepositSpecSheetPage from "./pages/general/deposit/DepositSpecSheetPage";
import FinalPage from "./pages/common/FinalPage";
import WithDrawalCardAuthPage from "./pages/general/withdrawal/WithDrawalCardAuthPage";
import GeneralMain from "./pages/GeneralMain";
import SeniorMain from "./pages/SeniorMain";
import TransferScamWarningPage from "./pages/general/transfer/TransferScamWarningPage";
import TransferCardWarningPage from "./pages/general/transfer/TransferCardWarningPage";
import TransferOptionPage from "./pages/general/transfer/TransferOptionPage";
import TransferCardInputPage from "./pages/general/transfer/TransferCardInputPage";
import { TransferCardAuthPage } from "./pages/general/transfer/TransferCardAuthPage";
import TransferCardPasswordPage from "./pages/general/transfer/TransferCardPasswordPage";
import TransferInfoAccountPage from "./pages/general/transfer/TransferInfoAccountPage";
import TransferInfoAmountPage from "./pages/general/transfer/TransferInfoAmountPage";
import TransferInfoSpecSheetPage from "./pages/general/transfer/TransferInfoSpecSheetPage";
import SeniorTransferScamWarningPage from "./pages/senior/transfer/SeniorTransferScamWarningPage";
import SeniorTransferOptionSeniorPage from "./pages/senior/transfer/SeniorTransferOptionPage";
import SeniorTransferCheckPage from "./pages/senior/transfer/SeniorTransferCheckPage";
import SeniorTransferCardWaringPage from "./pages/senior/transfer/SeniorTransferCardWarningPage";
import SeniorTransferCardInputPage from "./pages/senior/transfer/SeniorTransferCardInputPage";
import SeniorTransferCheckCardPage from "./pages/senior/transfer/SeniorTransferCheckCardPage";
import SeniorTransferCardPasswordPage from "./pages/senior/transfer/SeniorTransferCardPasswordPage";
import SeniorTransferInfoAccountPage from "./pages/senior/transfer/SeniorTransferInfoAccountPage";
import SeniorTransferInfoAmountPage from "./pages/senior/transfer/SeniorTransferInfoAmountPage";
import SeniorTransferInfoSpecSheetPage from "./pages/senior/transfer/SeniorTransferInfoSpecSheetPage";
import { SeniorFinalPage } from "./pages/senior/common/SeniorFinalPage";
import DepositScamWarningPage from "./pages/general/deposit/DepositScamWarningPage";
import DepositCardWarningPage from "./pages/general/deposit/DepositCardWarning";
import DepositCardInputPage from "./pages/general/deposit/DepositCardInputPage";
import DepositCardAuthPage from "./pages/general/deposit/DepositCardAuthPage";
import DepositCardPasswordPage from "./pages/general/deposit/DepositCardPasswordPage";
import DepositPaymentPage from "./pages/general/deposit/DepositPaymentPage";
import WithDrawalScamWarningPage from "./pages/general/withdrawal/WithDrawalScamWarningPage";
import WithDrawalCardWarningPage from "./pages/general/withdrawal/WithDrawalCardWarningPage";
import WithDrawalOptionPage from "./pages/general/withdrawal/WithDrawalOptionPage";
import WithDrawalCardPasswordPage from "./pages/general/withdrawal/WithDrawalCardPasswordPage";
import WithDrawalCashOutputPage from "./pages/general/withdrawal/WithDrawalCashOutputPage";
import WithDrawalInfoAmountPage from "./pages/general/withdrawal/WithDrawalInfoAmountPage";
import WithDrawalSpecSheetPage from "./pages/general/withdrawal/WithDrawalSpecSheetPage";
import WithDrawalCardInputPage from "./pages/general/withdrawal/WithDrawalCardInputPage";
import InquiryMainPage from "./pages/general/inquiry/InquiryMainPage";
import InquiryBalanceOptionPage from "./pages/general/inquiry/balance/InquiryBalanceOptionPage";
import InquiryBalanceCardWarningPage from "./pages/general/inquiry/balance/InquiryBalanceCardWarningPage";
import InquiryBalanceCardInputPage from "./pages/general/inquiry/balance/InquiryBalanceCardInputPage";
import InquiryBalanceCardAuthPage from "./pages/general/inquiry/balance/InquiryBalanceCardAuthPage";
import InquiryBalanceCardPasswordPage from "./pages/general/inquiry/balance/InquiryBalanceCardPasswordPage";
import InquiryBalanceSpecSheetPage from "./pages/general/inquiry/balance/InquiryBalanceSpecSheetPage";
import InquiryHistoryCardWarningPage from "./pages/general/inquiry/history/InquiryHistoryCardWarningPage";
import InquiryHistoryOptionPage from "./pages/general/inquiry/history/InquiryHistoryOptionPage";
import InquiryHistoryCardInputPage from "./pages/general/inquiry/history/InquiryHistoryCardInputPage";
import InquiryHistoryCardAuthPage from "./pages/general/inquiry/history/InquiryHistoryCardAuthPage";
import InquiryHistoryCardPasswordPage from "./pages/general/inquiry/history/InquiryHistoryCardPasswordPage";
import InquiryHistorySpecSheetPage from "./pages/general/inquiry/history/InquiryHistorySpecSheetPage";
import SeniorDepositScamWarningPage from "./pages/senior/deposit/SeniorDepositScamWarningPage";
import SeniorDepositCardWarningPage from "./pages/senior/deposit/SeniorDepositCardWarningPage";
import SeniorDepositOptionPage from "./pages/senior/deposit/SeniorDepositOptionPage";
import SeniorDepositCardInputPage from "./pages/senior/deposit/SeniorDepositCardInputPage";
import SeniorDepositCardAuthPage from "./pages/senior/deposit/SeniorDepositCardAuthPage";
import SeniorDepositCardPasswordPage from "./pages/senior/deposit/SeniorDepositCardPasswordPage";
import SeniorDepositPaymentPage from "./pages/senior/deposit/SeniorDepositPaymentPage";
import GeneralOtherPage from "./pages/general/GeneralOtherPage";
import DepositProducts from "./pages/general/products/deposit/DepositProducts";
import DepositProduct from "./pages/general/products/deposit/DepositProduct";
import DepositProductOption from "./pages/general/products/deposit/DepositProductOption";
import DepositProductCardWarningPage from "./pages/general/products/deposit/DepositProductCardWarningPage";
import DepositProductCardInputPage from "./pages/general/products/deposit/DepositProductCardInputPage";
import DepositProductCardAuthPage from "./pages/general/products/deposit/DepositProductCardAuthPage";
import DepositProductCardPasswordPage from "./pages/general/products/deposit/DepositProductCardPasswordPage";
import DepositProductInfoAmountPage from "./pages/general/products/deposit/DepositProductInfoAmountPage";
import DepositProductInfoSpecSheetPage from "./pages/general/products/deposit/DepositProductInfoSpecSheetPage";
import SeniorDepositSpecSheetPage from "./pages/senior/deposit/SeniorDepositSpecSheetPage";
import SeniorDepositConfirmPage from "./pages/senior/deposit/SeniorDepositConfirmPage";
import SeniorDepositCashCountingPage from "./pages/senior/deposit/SeniorDepositCashCountingPage";
import SeniorDepositCashInputPage from "./pages/senior/deposit/SeniorDepositCashInputPage";
import SeniorDepositCheckPage from "./pages/senior/deposit/SeniorDepositCheckPage";
import GeneralProductsPage from "./pages/general/products/GeneralProductsPage";
import SeniorDepositProductCheckPage from "./pages/senior/products/deposit/SeniorDepositProductCheckPage";
import SeniorDepositProductRecommendationPage from "./pages/senior/products/deposit/SeniorDepositProductRecommendationPage";
import SeniorDepositProductDicisionPage from "./pages/senior/products/deposit/SeniorDepositProductDicisionPage";
import SeniorDepositProductOption from "./pages/senior/products/deposit/SeniorDepositProductOptionPage";
import SeniorDepositProductCardInputPage from "./pages/senior/products/deposit/SeniorDepositProductCardInputPage";
import SeniorDepositProductCardAuthPage from "./pages/senior/products/deposit/SeniorDepositProductCardAuthPage";
import SeniorDepositProductCardPasswordPage from "./pages/senior/products/deposit/SeniorDepositProductCardPasswordPage";
import SeniorDepositProductInfoAmountPage from "./pages/senior/products/deposit/SeniorDepositProductInfoAmountPage";
import SeniorDepositProductInfoSpecSheetPage from "./pages/senior/products/deposit/SeniorDepositProductInfoSpecSheetPage";
import InstallmentProductInfoSpecSheetPage from "./pages/general/products/installment/InstallmentProductInfoSpecSheetPage";
import InstallmentProductInfoDayPage from "./pages/general/products/installment/InstallmentProductInfoDayPage";
import InstallmentProductInfoAmountPage from "./pages/general/products/installment/InstallmentProductInfoAmountPage";
import InstallmentProductCardPasswordPage from "./pages/general/products/installment/InstallmentProductCardPasswordPage";
import InstallmentProductCardAuthPage from "./pages/general/products/installment/InstallmentProductCardAuthPage";
import InstallmentProductCardInputPage from "./pages/general/products/installment/InstallmentProductCardInputPage";
import InstallmentProductOption from "./pages/general/products/installment/InstallmentProductOption";
import InstallmentProductCardWarningPage from "./pages/general/products/installment/InstallmentProductCardWarningPage";
import InstallmentProduct from "./pages/general/products/installment/InstallmentProduct";
import InstallmentProducts from "./pages/general/products/installment/InstallmentProducts";
import DemandProducts from "./pages/general/demands/DemandProducts";
import DemandProduct from "./pages/general/demands/DemandProduct";
import DemandProductResidentWarningPage from "./pages/general/demands/DemandProductResidentWarningPage";
import DemandProductResidentInputPage from "./pages/general/demands/DemandProductResidentInputPage";
import DemandProductResidentAuthPage from "./pages/general/demands/DemandProductResidentAuthPage";
import DemandProductInfoPasswordPage from "./pages/general/demands/DemandProductInfoPasswordPage";
import DemandProductInfoSpecSheetPage from "./pages/general/demands/DemandProductInfoSpecSheetPage";
import SeniorDepositProductCardWarningPage from "./pages/senior/products/deposit/SeniorDepositProductCardWarning";
import SeniorDepositProductProductQuestionPage from "./pages/senior/products/deposit/SeniorDepositProductProductQuestionPage";
import SeniorDepositProductRecommendationDetailPage from "./pages/senior/products/deposit/SeniorDepositProductRecommendationDetailPage";
import DepositAccountInputPage from "./pages/general/deposit/DepositAccountInputPage";
import DepositAccountPasswordPage from "./pages/general/deposit/DepositAccountPasswordPage";
import WithDrawalAccountInputPage from "./pages/general/withdrawal/WithDrawalAccountInputPage";
import WithDrawalAccountPasswordPage from "./pages/general/withdrawal/WithDrawalAccountPasswordPage";
import TransferAccountInputPage from "./pages/general/transfer/TransferAccountInputPage";
import TransferAccountPasswordPage from "./pages/general/transfer/TransferAccountPasswordPage";
import InquiryBalanceAccountInputPage from "./pages/general/inquiry/balance/InquiryBalanceAccountInputPage";
import InquiryBalanceAccountPasswordPage from "./pages/general/inquiry/balance/InquiryBalanceAccountPasswordPage";
import InquiryHistoryAccountInputPage from "./pages/general/inquiry/history/InquiryHistoryAccountInputPage";
import InquiryHistoryAccountPasswordPage from "./pages/general/inquiry/history/InquiryHistoryAccountPasswordPage";

const router = createHashRouter([
	{
		path: "/",
		element: <MainLayout />,
		errorElement: <NotFoundPage />,
		children: [
			{ index: true, element: <App /> },
			{
				path: "general",
				children: [
					{
						path: "",
						element: <GeneralMain />
					},
					{
						path: "others",
						element: <GeneralOtherPage />
					},
					{
						path: "deposit",
						children: [
							{
								path: "warning",
								children: [
									{
										path: "scam",
										element: <DepositScamWarningPage />
									},
									{
										path: "card",
										element: <DepositCardWarningPage />
									}
								]
							},
							{
								path: "option",
								element: <DepositOptionPage />
							},
							{
								path: "card",
								children: [
									{
										path: "input",
										element: <DepositCardInputPage />
									},
									{
										path: "auth",
										element: <DepositCardAuthPage />
									},
									{
										path: "password",
										element: <DepositCardPasswordPage />
									}
								]
							},
							{
								path: "account",
								children: [
									{
										path: "input",
										element: <DepositAccountInputPage />
									},
									{
										path: "password",
										element: <DepositAccountPasswordPage />
									}
								]
							},
							{
								path: "payment",
								element: <DepositPaymentPage />
							},
							{
								path: "cash",
								children: [
									{
										path: "input",
										element: <DepositCashInputPage />
									},
									{
										path: "counting",
										element: <DepositCashCountingPage />
									}
								]
							},
							{
								path: "confirm",
								element: <DepositConfirmPage />
							},
							{
								path: "specsheet",
								element: <DepositSpecSheetPage />
							}
						]
					},
					{
						path: "withdrawal",
						children: [
							{
								path: "warning",
								children: [
									{
										path: "scam",
										element: <WithDrawalScamWarningPage />
									},
									{
										path: "card",
										element: <WithDrawalCardWarningPage />
									}
								]
							},
							{
								path: "option",
								element: <WithDrawalOptionPage />
							},
							{
								path: "card",
								children: [
									{
										path: "input",
										element: <WithDrawalCardInputPage />
									},
									{
										path: "auth",
										element: <WithDrawalCardAuthPage />
									},
									{
										path: "password",
										element: <WithDrawalCardPasswordPage />
									}
								]
							},
							{
								path: "account",
								children: [
									{
										path: "input",
										element: <WithDrawalAccountInputPage />
									},
									{
										path: "password",
										element: <WithDrawalAccountPasswordPage />
									}
								]
							},
							{
								path: "cash",
								children: [
									{
										path: "output",
										element: <WithDrawalCashOutputPage />
									}
								]
							},
							{
								path: "info",
								children: [
									{
										path: "amount",
										element: <WithDrawalInfoAmountPage />
									},
									{
										path: "specsheet",
										element: <WithDrawalSpecSheetPage />
									}
								]
							}
						]
					},
					{
						path: "transfer",
						children: [
							{
								path: "warning",
								children: [
									{
										path: "scam",
										element: <TransferScamWarningPage />
									},
									{
										path: "card",
										element: <TransferCardWarningPage />
									}
								]
							},
							{
								path: "option",
								element: <TransferOptionPage />
							},
							{
								path: "card",
								children: [
									{
										path: "input",
										element: <TransferCardInputPage />
									},
									{
										path: "auth",
										element: <TransferCardAuthPage />
									},
									{
										path: "password",
										element: <TransferCardPasswordPage />
									}
								]
							},
							{
								path: "account",
								children: [
									{
										path: "input",
										element: <TransferAccountInputPage />
									},
									{
										path: "password",
										element: <TransferAccountPasswordPage />
									}
								]
							},
							{
								path: "info",
								children: [
									{
										path: "account",
										element: <TransferInfoAccountPage />
									},
									{
										path: "amount",
										element: <TransferInfoAmountPage />
									},
									{
										path: "specsheet",
										element: <TransferInfoSpecSheetPage />
									}
								]
							}
						]
					},
					{
						path: "inquiry",
						children: [
							{
								path: "option",
								element: <InquiryMainPage />
							}
						]
					},
					{
						path: "balance",
						children: [
							{
								path: "warning",
								children: [
									{
										path: "card",
										element: <InquiryBalanceCardWarningPage />
									}
								]
							},
							{
								path: "option",
								element: <InquiryBalanceOptionPage />
							},
							{
								path: "card",
								children: [
									{
										path: "input",
										element: <InquiryBalanceCardInputPage />
									},
									{
										path: "auth",
										element: <InquiryBalanceCardAuthPage />
									},
									{
										path: "password",
										element: <InquiryBalanceCardPasswordPage />
									}
								]
							},
							{
								path: "account",
								children: [
									{
										path: "input",
										element: <InquiryBalanceAccountInputPage />
									},
									{
										path: "password",
										element: <InquiryBalanceAccountPasswordPage />
									}
								]
							},
							{
								path: "specsheet",
								element: <InquiryBalanceSpecSheetPage />
							}
						]
					},
					{
						path: "history",
						children: [
							{
								path: "warning",
								children: [
									{
										path: "card",
										element: <InquiryHistoryCardWarningPage />
									}
								]
							},
							{
								path: "option",
								element: <InquiryHistoryOptionPage />
							},
							{
								path: "card",
								children: [
									{
										path: "input",
										element: <InquiryHistoryCardInputPage />
									},
									{
										path: "auth",
										element: <InquiryHistoryCardAuthPage />
									},
									{
										path: "password",
										element: <InquiryHistoryCardPasswordPage />
									}
								]
							},
							{
								path: "account",
								children: [
									{
										path: "input",
										element: <InquiryHistoryAccountInputPage />
									},
									{
										path: "password",
										element: <InquiryHistoryAccountPasswordPage />
									}
								]
							},
							{
								path: "specsheet",
								element: <InquiryHistorySpecSheetPage />
							}
						]
					},
					{
						path: "products",
						children: [
							{
								path: "option",
								element: <GeneralProductsPage />
							}
						]
					},
					{
						path: "depositproducts",
						children: [
							{
								path: "products",
								element: <DepositProducts />
							},
							{
								path: ":id",
								element: <DepositProduct />
							},
							{
								path: "warning",
								children: [
									{
										path: "card",
										element: <DepositProductCardWarningPage />
									}
								]
							},
							{
								path: "option",
								element: <DepositProductOption />
							},
							{
								path: "card",
								children: [
									{
										path: "input",
										element: <DepositProductCardInputPage />
									},
									{
										path: "auth",
										element: <DepositProductCardAuthPage />
									},
									{
										path: "password",
										element: <DepositProductCardPasswordPage />
									}
								]
							},
							{
								path: "info",
								children: [
									{
										path: "amount",
										element: <DepositProductInfoAmountPage />
									},
									{
										path: "specsheet",
										element: <DepositProductInfoSpecSheetPage />
									}
								]
							}
						]
					},
					{
						path: "installmentproducts",
						children: [
							{
								path: "products",
								element: <InstallmentProducts />
							},
							{
								path: ":id",
								element: <InstallmentProduct />
							},
							{
								path: "warning",
								children: [
									{
										path: "card",
										element: <InstallmentProductCardWarningPage />
									}
								]
							},
							{
								path: "option",
								element: <InstallmentProductOption />
							},
							{
								path: "card",
								children: [
									{
										path: "input",
										element: <InstallmentProductCardInputPage />
									},
									{
										path: "auth",
										element: <InstallmentProductCardAuthPage />
									},
									{
										path: "password",
										element: <InstallmentProductCardPasswordPage />
									}
								]
							},
							{
								path: "info",
								children: [
									{
										path: "amount",
										element: <InstallmentProductInfoAmountPage />
									},
									{
										path: "day",
										element: <InstallmentProductInfoDayPage />
									},
									{
										path: "specsheet",
										element: <InstallmentProductInfoSpecSheetPage />
									}
								]
							}
						]
					},
					{
						path: "demandproducts",
						children: [
							{
								path: "products",
								element: <DemandProducts />
							},
							{
								path: ":id",
								element: <DemandProduct />
							},
							{
								path: "warning",
								children: [
									{
										path: "residentwarning",
										element: <DemandProductResidentWarningPage />
									}
								]
							},
							{
								path: "resident",
								children: [
									{
										path: "input",
										element: <DemandProductResidentInputPage />
									},
									{
										path: "auth",
										element: <DemandProductResidentAuthPage />
									}
								]
							},
							{
								path: "info",
								children: [
									{
										path: "password",
										element: <DemandProductInfoPasswordPage />
									},
									{
										path: "specsheet",
										element: <DemandProductInfoSpecSheetPage />
									}
								]
							}
						]
					},
					{ path: "final", element: <FinalPage /> }
				]
			},
			{
				path: "senior",
				children: [
					{
						path: "",
						element: <SeniorMain />
					},
					{
						path: "transfer",
						children: [
							{
								path: "check",
								element: <SeniorTransferCheckPage />
							},
							{
								path: "warning",
								children: [
									{
										path: "scam",
										element: <SeniorTransferScamWarningPage />
									},
									{
										path: "card",
										element: <SeniorTransferCardWaringPage />
									}
								]
							},
							{
								path: "option",
								element: <SeniorTransferOptionSeniorPage />
							},
							{
								path: "card",
								children: [
									{
										path: "input",
										element: <SeniorTransferCardInputPage />
									},
									{
										path: "check",
										element: <SeniorTransferCheckCardPage />
									},
									{
										path: "password",
										element: <SeniorTransferCardPasswordPage />
									}
								]
							},
							{
								path: "info",
								children: [
									{
										path: "account",
										element: <SeniorTransferInfoAccountPage />
									},
									{
										path: "amount",
										element: <SeniorTransferInfoAmountPage />
									},
									{
										path: "specsheet",
										element: <SeniorTransferInfoSpecSheetPage />
									}
								]
							}
						]
					},
					{
						path: "deposit",
						children: [
							{
								path: "check",
								element: <SeniorDepositCheckPage />
							},
							{
								path: "warning",
								children: [
									{
										path: "scam",
										element: <SeniorDepositScamWarningPage />
									},
									{
										path: "card",
										element: <SeniorDepositCardWarningPage />
									}
								]
							},
							{
								path: "option",
								element: <SeniorDepositOptionPage />
							},
							{
								path: "card",
								children: [
									{
										path: "input",
										element: <SeniorDepositCardInputPage />
									},
									{
										path: "auth",
										element: <SeniorDepositCardAuthPage />
									},
									{
										path: "password",
										element: <SeniorDepositCardPasswordPage />
									}
								]
							},
							{
								path: "payment",
								element: <SeniorDepositPaymentPage />
							},
							{
								path: "cash",
								children: [
									{
										path: "input",
										element: <SeniorDepositCashInputPage />
									},
									{
										path: "count",
										element: <SeniorDepositCashCountingPage />
									}
								]
							},
							{
								path: "confirm",
								element: <SeniorDepositConfirmPage />
							},
							{
								path: "specsheet",
								element: <SeniorDepositSpecSheetPage />
							}
						]
					},
					{
						path: "depositproducts",
						children: [
							{
								path: "check",
								element: <SeniorDepositProductCheckPage />
							},
							{
								path: "question",
								element: <SeniorDepositProductProductQuestionPage />
							},
							{
								path: "recommendation",
								children: [
									{
										path: "question",
										element: <SeniorDepositProductProductQuestionPage />
									},
									{
										path: "feature",
										element: <SeniorDepositProductRecommendationPage />
									},
									{
										path: "detail",
										element: <SeniorDepositProductRecommendationDetailPage />
									},
									{
										path: "specsheet",
										element: <SeniorDepositSpecSheetPage />
									}
								]
							},
							{
								path: "dicision",
								element: <SeniorDepositProductDicisionPage />
							},
							{
								path: "option",
								element: <SeniorDepositProductOption />
							},
							{
								path: "warning",
								children: [
									{
										path: "card",
										element: <SeniorDepositProductCardWarningPage />
									}
								]
							},
							{
								path: "card",
								children: [
									{
										path: "input",
										element: <SeniorDepositProductCardInputPage />
									},
									{
										path: "auth",
										element: <SeniorDepositProductCardAuthPage />
									},
									{
										path: "password",
										element: <SeniorDepositProductCardPasswordPage />
									}
								]
							},
							{
								path: "info",
								children: [
									{
										path: "amount",
										element: <SeniorDepositProductInfoAmountPage />
									},
									{
										path: "specsheet",
										element: <SeniorDepositProductInfoSpecSheetPage />
									}
								]
							}
						]
					},
					{ path: "final", element: <SeniorFinalPage /> }
				]
			}
		]
	}
]);

export default router;
